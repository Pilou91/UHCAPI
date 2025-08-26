package fr.pilou.uhcapi.game.scenarios.impl;

import fr.pilou.uhcapi.game.scenarios.Scenario;
import fr.pilou.uhcapi.game.scenarios.ScenarioRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@ScenarioRegistry(name = "Safe Miners", material = Material.LAVA_BUCKET)
public class SafeMinersScenario extends Scenario implements Listener {

    @EventHandler
    public void onFireDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player player)){
            return;
        }

        if(player.getLocation().getY() > 35){
            return;
        }

        if(event.getCause().equals(EntityDamageEvent.DamageCause.LAVA) ||
                event.getCause().equals(EntityDamageEvent.DamageCause.FIRE)
                || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)){
            event.setCancelled(true);
        }

    }
}
