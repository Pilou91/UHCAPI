package fr.pilou.uhcapi.listeners;

import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.State;
import fr.pilou.uhcapi.game.player.IGamePlayer;
import fr.pilou.uhcapi.utils.CC;
import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

@ListenerRegistry(state = {State.IN_GAME, State.FINISH})
public class GameListener implements Listener {
    private final IGameHandler gameHandler = API.get().getGameHandler();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        IGamePlayer gamePlayer = gameHandler.getGamePlayer(player.getUniqueId());
        if(!gamePlayer.isAlive()){
            player.setGameMode(GameMode.SPECTATOR);
            event.setJoinMessage("");
            return;
        }

        int disconnectTime = gamePlayer.getDisconnectTime();
        event.setJoinMessage(CC.uhcPrefix("§e" + player.getName() + " §rs'est §areconnecté§r. (§a" + CC.time(disconnectTime) + "§r)"));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        IGamePlayer gamePlayer = gameHandler.getGamePlayer(player.getUniqueId());
        if(!gamePlayer.isAlive()){
            event.setQuitMessage("");
            return;
        }

        int disconnectTime = gamePlayer.getDisconnectTime();
        event.setQuitMessage(CC.uhcPrefix("§e" + player.getName() + " §rs'est §cdéconnecté§r. (§c" + CC.time(disconnectTime) + "§r)"));
    }

    @EventHandler
    public void onPlayerAchievementAwarded(PlayerAchievementAwardedEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event){
        if(gameHandler.isPvp()){
            return;
        }

        if(!(event.getEntity() instanceof Player)){
            return;
        }

        if(!(event.getDamager() instanceof Arrow) && !(event.getDamager() instanceof Player)){
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        IGameHandler gameHandler = API.get().getGameHandler();

        Player player = event.getEntity();
        IGamePlayer gamePlayer = gameHandler.getGamePlayer(player.getUniqueId());

        event.setDeathMessage("");

        Player killer = player.getKiller();
        gamePlayer.die(killer, player.getInventory());
    }
}
