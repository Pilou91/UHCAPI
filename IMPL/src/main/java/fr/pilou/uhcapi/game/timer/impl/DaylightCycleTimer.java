package fr.pilou.uhcapi.game.timer.impl;

import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.timers.Timer;
import fr.pilou.uhcapi.game.timers.TimerRegistry;
import fr.pilou.uhcapi.listeners.DaylightCycleEvent;
import fr.pilou.uhcapi.listeners.EpisodeEvent;
import fr.pilou.uhcapi.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@TimerRegistry(name = "Cycle Jour/Nuit", activationTime = 15)
public class DaylightCycleTimer extends Timer {
    private int episode = 0;
    private boolean day = false;

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.DAYLIGHT_DETECTOR);
    }


    @Override
    public void run(int currentTime) {
        if(currentTime == 0 || currentTime % (getActivationTime() * 4) == 0){
            episode++;
            Bukkit.broadcastMessage(CC.uhcPrefix("§e§lEpisode " + episode));
            Bukkit.getScheduler().runTaskLater(API.get().getPlugin(), () -> {
                Bukkit.getPluginManager().callEvent(new EpisodeEvent(episode));
            }, 20L);
        }

        if (currentTime != 0 && currentTime % getActivationTime() != 0) {
            return;
        }

        day = !day;
        Bukkit.getScheduler().runTaskLater(API.get().getPlugin(), () -> {
            Bukkit.getPluginManager().callEvent(new DaylightCycleEvent(day));
        }, 20L);

        if (day) {
            Bukkit.broadcastMessage(CC.uhcPrefix("§6Le soleil se lève ✹"));
            return;
        }

        Bukkit.broadcastMessage(CC.uhcPrefix("§9Le nuit tombe ☽"));
    }
}
