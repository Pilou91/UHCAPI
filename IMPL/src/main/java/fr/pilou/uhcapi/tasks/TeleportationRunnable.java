package fr.pilou.uhcapi.tasks;

import com.google.common.collect.Lists;
import fr.pilou.potionapi.potions.CustomPotionType;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.player.IGamePlayer;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.State;
import fr.pilou.uhcapi.game.world.IWorldHandler;
import fr.pilou.uhcapi.utils.ComponentUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TeleportationRunnable extends BukkitRunnable {
    private final IGameHandler gameHandler;
    private final List<IGamePlayer> toTeleportList;
    private final int amountToTeleport;
    private int teleportAmount = 0;

    public TeleportationRunnable(IGameHandler gameHandler){
        this.gameHandler = gameHandler;
        this.toTeleportList = Lists.newArrayList(gameHandler.getGamePlayers());
        this.amountToTeleport = toTeleportList.size();
        gameHandler.setState(State.TELEPORT);
    }

    @Override
    public void run() {
        if(teleportAmount == amountToTeleport){
            new BukkitRunnable(){
                @Override
                public void run() {
                    for (IGamePlayer gamePlayer : gameHandler.getGamePlayers()) {
                        gamePlayer.clearPotionsEffects();
                    }
                    gameHandler.inGame();
                }
            }.runTaskLater(API.get().getPlugin(), 3 * 20L);
            cancel();
            return;
        }

        int index = ThreadLocalRandom.current().nextInt(toTeleportList.size());
        IGamePlayer gamePlayer = toTeleportList.get(index);
        toTeleportList.remove(gamePlayer);
        Player player = gamePlayer.getPlayer();
        if(player != null){
            IWorldHandler worldHandler = API.get().getWorldHandler();
            World world = worldHandler.getWorld();
            int size = worldHandler.getWorldBorderSize();
            int x = ThreadLocalRandom.current().nextInt(-size, size);
            int z = ThreadLocalRandom.current().nextInt(-size, size);
            player.teleport(new Location(world, x, world.getHighestBlockYAt(x, z), z));
            gamePlayer.addPotion(CustomPotionType.SLOWNESS, 255);
            gamePlayer.addPotion(CustomPotionType.BLINDNESS, 255);
            gamePlayer.addPotion(CustomPotionType.JUMP_BOOST, 120);
        }

        teleportAmount++;

        for (Player target : Bukkit.getOnlinePlayers()) {
            target.playSound(target.getLocation(), Sound.CLICK, 1f, 1f);
            ComponentUtils.sendActionBar(target, "§f» §eTéléportation : §f" + teleportAmount + "§e/§f" + amountToTeleport + " §f«");
        }
    }
}
