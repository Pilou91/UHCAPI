package fr.pilou.uhcapi.game.scenarios;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface IScenariosHandler {
    List<Scenario> getScenarios();

    List<Scenario> getEnabledScenarios();

    void registerScenarios(JavaPlugin plugin);

    boolean isEnabled(Scenario scenario);

    void enableScenario(Scenario scenario);

    void disableScenario(Scenario scenario);

    void handleScenarios();
}
