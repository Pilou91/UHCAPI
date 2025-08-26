package fr.pilou.uhcapi.listeners;

import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.State;
import fr.pilou.uhcapi.game.player.IGamePlayer;
import fr.pilou.uhcapi.items.ClickableItems;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

@ListenerRegistry(state = {State.WAITING, State.STARTING, State.TELEPORT})
public class WaitingListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1f, 1f);
        event.setJoinMessage("§r(§a»§r) §a" + player.getName() + " §ra rejoint.");

        IGameHandler gameHandler = API.get().getGameHandler();
        IGamePlayer gamePlayer = gameHandler.getGamePlayer(player.getUniqueId());

        if(player.isOp()){
            gameHandler.setHost(player.getName());
        }

        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0f);
        player.setLevel(0);
        player.setFireTicks(0);
        player.setWalkSpeed(0.2F);
        player.setGameMode(GameMode.SURVIVAL);
        player.setFlying(false);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }

        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setHelmet(null);
        inventory.setChestplate(null);
        inventory.setLeggings(null);
        inventory.setBoots(null);

        gamePlayer.loadHotBar();
        player.updateInventory();

        player.teleport(new Location(Bukkit.getWorld("world"), 0, 100, 0));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        event.setQuitMessage("§r(§c«§r) §c" + player.getName() + " §ra quitté.");

        IGameHandler gameHandler = API.get().getGameHandler();
        IGamePlayer gamePlayer = gameHandler.getGamePlayer(player.getUniqueId());
        gameHandler.getGamePlayers().remove(gamePlayer);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event){
        if(!event.getEntity().getWorld().getName().equals("world")){
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerAchievementAwarded(PlayerAchievementAwardedEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)){
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }
}
