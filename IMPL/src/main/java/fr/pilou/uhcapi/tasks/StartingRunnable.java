package fr.pilou.uhcapi.tasks;

import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.State;
import fr.pilou.uhcapi.utils.ComponentUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StartingRunnable extends BukkitRunnable {
    private final IGameHandler gameHandler = API.get().getGameHandler();
    private int timeLeft = 5;

    @Override
    public void run() {
        if(gameHandler.getState() == State.WAITING){
            for (Player player : Bukkit.getOnlinePlayers()) {
                ComponentUtils.sendTitle(player, "§c» §rLancement §c«", "§cArrêter", 1, 20, 1);
                player.playSound(player.getLocation(), Sound.ANVIL_USE, 1f, 1f);
            }
            cancel();
            return;
        }

        if (timeLeft <= 0) {
            new TeleportationRunnable(gameHandler).runTaskTimer(API.get().getPlugin(), 0L, 5L);
            cancel();
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            ComponentUtils.sendTitle(player, "§e» §rLancement §e«", "§e" + timeLeft + " §rseconde" + (timeLeft > 1 ? "s" : ""), 1, 20, 1);
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1f, 0.5f);
        }

        timeLeft--;
    }
}
