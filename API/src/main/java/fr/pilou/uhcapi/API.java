package fr.pilou.uhcapi;

import ca.kaxx.board.KaxxScoreboardHandler;
import com.google.common.base.Preconditions;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.inventory.IInventoryHandler;
import fr.pilou.uhcapi.game.scenarios.IScenariosHandler;
import fr.pilou.uhcapi.game.timers.ITimerHandler;
import fr.pilou.uhcapi.game.world.IBiomeGeneratorHandler;
import fr.pilou.uhcapi.game.world.IWorldHandler;
import fr.pilou.uhcapi.module.IModuleHandler;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public abstract class API {
    private static API implementation;
    private final JavaPlugin plugin;

    public static API get(){
        return implementation;
    }

    public abstract IGameHandler getGameHandler();
    public abstract IWorldHandler getWorldHandler();
    public abstract IBiomeGeneratorHandler getBiomeGeneratorHandler();
    public abstract IScenariosHandler getScenariosHandler();
    public abstract ITimerHandler getTimerHandler();
    public abstract IInventoryHandler getInventoryHandler();
    public abstract IModuleHandler getModuleHandler();
    public abstract KaxxScoreboardHandler getScoreboardHandler();

    protected API(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static synchronized void initializeImplementation(final API api) {
        Preconditions.checkState(implementation == null, "Implementation is already set");
        API.implementation = api;
    }

    public void onLoad(){

    }

    public void onEnable(){

    }

    public void onDisable(){

    }
}