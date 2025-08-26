package fr.pilou.uhcapi.game.scenarios.impl;

import fr.pilou.uhcapi.game.scenarios.Scenario;
import fr.pilou.uhcapi.game.scenarios.ScenarioRegistry;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

@ScenarioRegistry(name = "Hastey Boys", material = Material.IRON_PICKAXE)
public class HasteyBoysScenario extends Scenario implements Listener {

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        Material type = item.getType();
        String name = type.toString();

        if (!(name.endsWith("AXE") || name.endsWith("PICKAXE") || name.endsWith("SPADE"))) {
            return;
        }

        ItemStack enchanted = item.clone();
        enchanted.addEnchantment(Enchantment.DIG_SPEED, 3);
        enchanted.addEnchantment(Enchantment.DURABILITY, 3);

        event.getInventory().setResult(enchanted);
    }
}
