package fr.pilou.uhcapi.game.timers;

import fr.pilou.uhcapi.API;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;

@Getter
public abstract class Timer {
    private final ITimerHandler timerHandler = API.get().getTimerHandler();
    private String name;
    private int defaultActivationTime;

    @SneakyThrows
    public Timer(){
        TimerRegistry registry = getClass().getDeclaredAnnotation(TimerRegistry.class);
        if (registry == null) {
            throw new Exception("Scenario data must not be null");
        }

        this.name = registry.name();
        this.defaultActivationTime = registry.activationTime();
    }

    public abstract ItemStack getIcon();

    public int getActivationTime(){
        return timerHandler.getActivationTime().getOrDefault(getName(), getDefaultActivationTime());
    }

    public void setActivationTime(int time){
        timerHandler.getActivationTime().put(getName(), time);
    }


    public abstract void run(int currentTime);
}
