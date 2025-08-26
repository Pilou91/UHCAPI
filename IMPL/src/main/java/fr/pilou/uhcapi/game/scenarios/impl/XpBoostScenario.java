package fr.pilou.uhcapi.game.scenarios.impl;

import fr.pilou.uhcapi.game.scenarios.Scenario;
import fr.pilou.uhcapi.game.scenarios.ScenarioRegistry;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@ScenarioRegistry(name = "Xp Boost", material = Material.EXP_BOTTLE)
public class XpBoostScenario extends Scenario {
    public final double expBoost = 3;
}
