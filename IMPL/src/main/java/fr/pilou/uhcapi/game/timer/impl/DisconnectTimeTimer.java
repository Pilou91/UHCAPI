package fr.pilou.uhcapi.game.timer.impl;

import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.player.IGamePlayer;
import fr.pilou.uhcapi.game.timers.Timer;
import fr.pilou.uhcapi.game.timers.TimerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@TimerRegistry(name = "Temps de déconnexion", activationTime = 0)
public class DisconnectTimeTimer extends Timer {
    @Override
    public ItemStack getIcon() {
        return null;
    }

    @Override
    public void run(int currentTime) {
        IGameHandler gameHandler = API.get().getGameHandler();
        for (IGamePlayer gamePlayer : gameHandler.getGamePlayers()) {
            Player player = gamePlayer.getPlayer();
            if (player.isOnline() || gamePlayer.getDisconnectTime() == -1) {
                continue;
            }

            if(gamePlayer.getDisconnectTime() == 0){
                gamePlayer.die(null, player.getInventory());
            }

            gamePlayer.setDisconnectTime(gamePlayer.getDisconnectTime() - 1);
        }
    }
}
