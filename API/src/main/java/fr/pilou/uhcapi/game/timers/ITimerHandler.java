package fr.pilou.uhcapi.game.timers;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public interface ITimerHandler {
    Map<String, Integer> getActivationTime();

    List<Timer> getTimers();

    void registerTimers(JavaPlugin plugin);
}
