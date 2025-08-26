package fr.pilou.uhcapi.game.scenarios.impl;

import com.google.common.collect.Maps;
import fr.pilou.uhcapi.game.scenarios.Scenario;
import fr.pilou.uhcapi.game.scenarios.ScenarioRegistry;
import fr.pilou.uhcapi.utils.ComponentUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

@ScenarioRegistry(name = "Diamond Limit", material = Material.DIAMOND)
public class DiamondLimitScenario extends Scenario implements Listener {
    private final int diamondLimit = 22;
    private final Map<UUID, Integer> diamondLimitMap = Maps.newHashMap();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();
        World world = block.getWorld();
        int expToDrop = event.getExpToDrop();

        if(block.getType() != Material.DIAMOND_ORE){
            return;
        }

        UUID uuid = player.getUniqueId();
        int diamondCount = diamondLimitMap.getOrDefault(uuid, 1);
        if(diamondCount > 22){
            ComponentUtils.sendActionBar(player, "§f» §3Limite de Diamants : §cDépassée");
            event.setCancelled(true);
            block.setType(Material.AIR);

            Location dropLocation = location.clone().add(0.5, 0, 0.5);
            world.dropItemNaturally(dropLocation, new ItemStack(Material.GOLD_INGOT));

            ExperienceOrb experienceOrb = world.spawn(dropLocation, ExperienceOrb.class);
            experienceOrb.setExperience(expToDrop);
            return;
        }

        diamondLimitMap.put(uuid, diamondCount + 1);
        ComponentUtils.sendActionBar(player, "§f» §3Limite de Diamants : §r" + diamondCount + "/" + diamondLimit);
    }
}
