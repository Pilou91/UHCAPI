package fr.pilou.uhcapi.game.scenarios.impl;

import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.player.IGamePlayer;
import fr.pilou.uhcapi.game.scenarios.Scenario;
import fr.pilou.uhcapi.game.scenarios.ScenarioRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@ScenarioRegistry(name = "Cat Eyes", material = Material.EYE_OF_ENDER)
public class CatEyesScenario extends Scenario {
    @Override
    public void handle(IGameHandler gameHandler) {
        for (IGamePlayer gamePlayer : gameHandler.getGamePlayers()) {
            Player player = gamePlayer.getPlayer();
            if(player == null){
                continue;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false), true);
        }
    }
}
