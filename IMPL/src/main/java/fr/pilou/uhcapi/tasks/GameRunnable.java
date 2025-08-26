package fr.pilou.uhcapi.tasks;

import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.GameHandler;
import fr.pilou.uhcapi.game.timers.ITimerHandler;
import org.bukkit.scheduler.BukkitRunnable;

public class GameRunnable extends BukkitRunnable {
    private final ITimerHandler timerHandler = API.get().getTimerHandler();
    private final GameHandler gameHandler;
    private int ticks = 0;

    public GameRunnable(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    @Override
    public void run() {
        ticks++;
        if(ticks != 0 && ticks < 20){
            return;
        }

        ticks = 0;
        gameHandler.setCurrentTime(gameHandler.getCurrentTime() + 1);
        timerHandler.getTimers().forEach(timer -> timer.run(gameHandler.getCurrentTime()));
    }
}
