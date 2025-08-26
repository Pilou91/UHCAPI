package fr.pilou.uhcapi.game.player;

import com.google.common.collect.Maps;
import fr.pilou.potionapi.PotionAPI;
import fr.pilou.potionapi.potions.CustomPotionType;
import fr.pilou.potionapi.potions.Potion;
import fr.pilou.potionapi.profile.Profile;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.items.ClickableItems;
import fr.pilou.uhcapi.module.IModuleHandler;
import fr.pilou.uhcapi.module.camp.Camp;
import fr.pilou.uhcapi.module.role.Role;
import fr.pilou.uhcapi.utils.CC;
import fr.pilou.uhcapi.utils.Color;
import fr.pilou.uhcapi.utils.ComponentUtils;
import fr.pilou.uhcapi.utils.tag.TagUtils;
import lombok.Data;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class GamePlayer implements IGamePlayer {
    private final UUID uniqueId;
    private final Player player;
    private final String name;
    private boolean alive = true;
    private int kills, assists;
    private int disconnectTime;
    private final Map<UUID, String> colors;
    private boolean inventoryEdit;
    private PlayerInventory deathInventory;
    private Role role;

    public GamePlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.player = Bukkit.getPlayer(uniqueId);
        this.name = player.getName();
        this.disconnectTime = 15 * 60;
        this.colors = Maps.newHashMap();
    }

    @Override
    public void sendMessage(String message) {
        if (player == null) {
            return;
        }

        player.sendMessage(message);
    }

    @Override
    public void loadHotBar() {
        ItemStack icon = ClickableItems.MAIN.getClickableItem().getIcon();
        player.getInventory().setItem(4, icon);
    }

    public void setColor(Player target, Color color) {
        TagUtils.addPrefix(target, "§" + color.getColor().getChar(), target.getName(), Integer.MAX_VALUE, List.of(uniqueId));
    }

    @Override
    public void addPotion(CustomPotionType type, int level){
        Profile profile = PotionAPI.getProfile(uniqueId);
        profile.addPotion(type, level);
    }

    @Override
    public void addPotion(CustomPotionType type, int level, int duration){
        Profile profile = PotionAPI.getProfile(uniqueId);
        profile.addPotion(type, level, duration);
    }

    @Override
    public void removePotion(CustomPotionType type){
        Profile profile = PotionAPI.getProfile(uniqueId);
        profile.removePotion(type);
    }

    @Override
    public void clearPotionsEffects() {
        Profile profile = PotionAPI.getProfile(uniqueId);
        for (Potion potion : profile.getPotions()) {
            removePotion(potion.getType());
        }
    }

    @Override
    public void sendPlayerNameOfRole(Class<? extends Role> roleClass) {
        new BukkitRunnable() {
            @SneakyThrows
            @Override
            public void run() {
                IModuleHandler moduleHandler = API.get().getModuleHandler();
                Role role = roleClass.getDeclaredConstructor().newInstance();
                IGamePlayer gamePlayer = moduleHandler.getPlayerNameOfRole(role);

                String name = gamePlayer == null ? "§cJoueur introuvable" : gamePlayer.getName();
                Camp camp = role.getCampClass().getDeclaredConstructor().newInstance();
                ChatColor campColor = camp.getColor();

                sendMessage(CC.modulePrefix(campColor + role.getName() + " : §r" + name));

                if (gamePlayer == null) {
                    return;
                }

                Player target = gamePlayer.getPlayer();
                if (target == null) {
                    return;
                }

                Color color = Color.getFromChatColor(campColor);
                setColor(target, color);
            }
        }.runTaskLater(API.get().getPlugin(), 10L);
    }

    @SneakyThrows
    @Override
    public void die(Player killer, PlayerInventory inventory) {
        setAlive(false);

        if (player != null) {
            setDeathInventory(inventory);

            Location location = player.getLocation();

            if (role != null) {
                Class<? extends Camp> campClass = role.getCampClass();
                Camp camp = campClass.getDeclaredConstructor().newInstance();

                Bukkit.broadcastMessage("§e»§f§l§m------------------------------------------§e«");
                Bukkit.broadcastMessage("§f");
                Bukkit.broadcastMessage(CC.modulePrefix("§e§l" + name + " §rest mort. Il était " + camp.getColor() + role.getName()));
                Bukkit.broadcastMessage("§f");
                Bukkit.broadcastMessage("§e»§f§l§m------------------------------------------§e«");
            }

            Bukkit.getScheduler().runTaskLater(API.get().getPlugin(), () -> {
                player.spigot().respawn();
                player.teleport(location);
                player.setGameMode(GameMode.SPECTATOR);
                ComponentUtils.sendTitle(player, "Vous êtes §c§lMort", "", 20, 20, 20);
            }, 5L);
        }

        if (killer == null) {
            return;
        }

        IGameHandler gameHandler = API.get().getGameHandler();
        IGamePlayer gamePlayer = gameHandler.getGamePlayer(killer.getUniqueId());
        gamePlayer.setKills(gamePlayer.getKills() + 1);
    }

    @Override
    public void revive() {
        if (player == null) {
            return;
        }

        Location currentLocation = player.getLocation();
        Location location = null;
        for (int y = (int) currentLocation.getY(); y < 0; y++) {
            Location loc = new Location(currentLocation.getWorld(), currentLocation.getX(), y, currentLocation.getZ());
            Block block = loc.getBlock();
            if (!block.getType().isSolid() || block.isLiquid()) {
                continue;
            }

            location = loc;
        }

        if (location != null) {
            player.teleport(location);
        }

        player.setGameMode(GameMode.SURVIVAL);
        setAlive(true);

        player.getInventory().clear();

        player.getInventory().setArmorContents(getDeathInventory().getArmorContents());
        player.getInventory().setContents(getDeathInventory().getContents());

        player.updateInventory();
    }
}
