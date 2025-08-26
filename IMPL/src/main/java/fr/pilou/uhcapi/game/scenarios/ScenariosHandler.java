package fr.pilou.uhcapi.game.scenarios;

import com.google.common.collect.Lists;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.State;
import fr.pilou.uhcapi.utils.ReflectionUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public class ScenariosHandler implements IScenariosHandler{
    private final List<Scenario> scenarios;
    private final List<Scenario> enabledScenarios;

    public ScenariosHandler(){
        this.scenarios = Lists.newArrayList();
        this.enabledScenarios = Lists.newArrayList();
    }

    @Override
    public void registerScenarios(JavaPlugin plugin) {
        List<Class<?>> classes = ReflectionUtils.getClassWithAnnotation(plugin, ScenarioRegistry.class);

        for (Class<?> clazz : classes) {
            try {
                if (!Scenario.class.isAssignableFrom(clazz)) continue;

                Scenario scenario = (Scenario) clazz.getDeclaredConstructor().newInstance();
                scenarios.add(scenario);
                System.out.println("(UHC API) Registered scenario: " + scenario.getName());

            } catch (Exception e) {
                System.err.println("Failed to register scenario class: " + clazz.getName());
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isEnabled(Scenario scenario) {
        for (Scenario enabledScenario : enabledScenarios) {
            if(enabledScenario.getClass().isInstance(scenario)){
                return true;
            }
        }

        return false;
    }

    @Override
    public void enableScenario(Scenario scenario) {
        enabledScenarios.add(scenario);
        IGameHandler gameHandler = API.get().getGameHandler();
        if(gameHandler.getState() != State.IN_GAME && gameHandler.getState() != State.FINISH){
            return;
        }

        if (!(scenario instanceof Listener listener)){
            return;
        }

        Bukkit.getPluginManager().registerEvents(listener, API.get().getPlugin());
    }

    @Override
    public void disableScenario(Scenario scenario) {
        enabledScenarios.remove(scenario);
        if (!(scenario instanceof Listener listener)){
            return;
        }

        HandlerList.unregisterAll(listener);
    }

    @Override
    public void handleScenarios() {
        for (Scenario scenario : getEnabledScenarios()) {
            scenario.handle(API.get().getGameHandler());
            if (!(scenario instanceof Listener listener)){
                continue;
            }

            Bukkit.getPluginManager().registerEvents(listener, API.get().getPlugin());
        }
    }
}
