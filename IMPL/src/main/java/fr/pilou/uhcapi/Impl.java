package fr.pilou.uhcapi;


import ca.kaxx.board.KaxxScoreboardHandler;
import ca.kaxx.board.animation.ScoreboardAnimation;
import fr.pilou.uhcapi.board.WaitingBoard;
import fr.pilou.uhcapi.game.GameHandler;
import fr.pilou.uhcapi.game.State;
import fr.pilou.uhcapi.game.inventory.IInventoryHandler;
import fr.pilou.uhcapi.game.inventory.InventoryHandler;
import fr.pilou.uhcapi.game.scenarios.IScenariosHandler;
import fr.pilou.uhcapi.game.scenarios.ScenariosHandler;
import fr.pilou.uhcapi.game.timer.TimerHandler;
import fr.pilou.uhcapi.game.timers.ITimerHandler;
import fr.pilou.uhcapi.game.world.BiomeGeneratorHandler;
import fr.pilou.uhcapi.game.world.WorldHandler;
import fr.pilou.uhcapi.module.IModuleHandler;
import fr.pilou.uhcapi.module.ModuleHandler;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Impl extends API{
    private final GameHandler gameHandler;
    private final WorldHandler worldHandler;
    private final BiomeGeneratorHandler biomeGeneratorHandler;
    private final IScenariosHandler scenariosHandler;
    private final ITimerHandler timerHandler;
    private final IInventoryHandler inventoryHandler;
    private final IModuleHandler moduleHandler;
    private KaxxScoreboardHandler scoreboardHandler;

    Impl(JavaPlugin plugin){
        super(plugin);
        gameHandler = new GameHandler();
        worldHandler = new WorldHandler();
        biomeGeneratorHandler = new BiomeGeneratorHandler();
        scenariosHandler = new ScenariosHandler();
        timerHandler = new TimerHandler();
        inventoryHandler = new InventoryHandler();
        moduleHandler = new ModuleHandler();
    }

    @Override
    public void onEnable() {
        scoreboardHandler = KaxxScoreboardHandler.create(API.get().getPlugin());
        scoreboardHandler.setRefreshRate(1);
        scoreboardHandler.setScoreboardAnimation(new ScoreboardAnimation("@Pilou91", ChatColor.YELLOW, ChatColor.WHITE));
        scoreboardHandler.setAdapter(new WaitingBoard());
        scenariosHandler.registerScenarios(API.get().getPlugin());
        timerHandler.registerTimers(API.get().getPlugin());

        gameHandler.setState(State.WAITING);
    }

    @Override
    public void onDisable() {
        getWorldHandler().deleteWorld();
        biomeGeneratorHandler.getBiomeSwapper().resetBiomes();
    }
}
