package fr.pilou.uhcapi.game.timer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.scenarios.Scenario;
import fr.pilou.uhcapi.game.scenarios.ScenarioRegistry;
import fr.pilou.uhcapi.game.timers.ITimerHandler;
import fr.pilou.uhcapi.game.timers.Timer;
import fr.pilou.uhcapi.game.timers.TimerRegistry;
import fr.pilou.uhcapi.utils.ReflectionUtils;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

@Getter
public class TimerHandler implements ITimerHandler {
    private final List<Timer> timers = Lists.newArrayList();
    private final Map<String, Integer> activationTime;

    public TimerHandler() {
        activationTime = Maps.newHashMap();
    }

    @Override
    public void registerTimers(JavaPlugin plugin) {
        List<Class<?>> classes = ReflectionUtils.getClassWithAnnotation(plugin, TimerRegistry.class);

        for (Class<?> clazz : classes) {
            try {
                if (!Timer.class.isAssignableFrom(clazz)) continue;

                Timer timer = (Timer) clazz.getDeclaredConstructor().newInstance();
                timers.add(timer);
                System.out.println("(UHC API) Registered timer: " + timer.getName());

            } catch (Exception e) {
                System.err.println("Failed to register timer class: " + clazz.getName());
                e.printStackTrace();
            }
        }
    }
}
