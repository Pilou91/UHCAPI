package fr.pilou.uhcapi.game.scenarios.impl;

import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.scenarios.IScenariosHandler;
import fr.pilou.uhcapi.game.scenarios.Scenario;
import fr.pilou.uhcapi.game.scenarios.ScenarioRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
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
import org.bukkit.inventory.PlayerInventory;

@ScenarioRegistry(name = "Cut Clean", material = Material.IRON_INGOT)
public class CutCleanScenario extends Scenario implements Listener {
    public boolean havePlaceForItemStack(Player player, ItemStack itemStack){
        PlayerInventory inventory = player.getInventory();
        for (ItemStack contents : inventory) {
            if(contents == null) {
                return true;
            }
            if(contents.getType() == itemStack.getType() && contents.getAmount() < contents.getMaxStackSize()){
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        IScenariosHandler scenariosHandler = API.get().getScenariosHandler();

        Player player = event.getPlayer();

        Block block = event.getBlock();
        World world = block.getWorld();
        Location location = block.getLocation();

        Material from = block.getType();
        Material to = to(from);

        if (to == null) {
            return;
        }

        event.setCancelled(true);
        block.setType(Material.AIR);

        boolean oreMagnet = scenariosHandler.isEnabled(new OreMagnetScenario());
        Location dropLocation = location.clone().add(0.5, 0, 0.5);
        ItemStack itemStack = new ItemStack(to);
        if (isOreMagnetAvailable(from) && oreMagnet && havePlaceForItemStack(player, itemStack)) {
            player.getInventory().addItem(itemStack);
        } else {
            world.dropItemNaturally(dropLocation, itemStack);
        }

        int xp = xp(from);
        if (xp == 0) {
            return;
        }

        ExperienceOrb experienceOrb = world.spawn(dropLocation, ExperienceOrb.class);

        XpBoostScenario xpBoostScenario = new XpBoostScenario();
        boolean enabled = scenariosHandler.isEnabled(xpBoostScenario);

        experienceOrb.setExperience((int) (xp * (enabled ? xpBoostScenario.getExpBoost() : 1)));
    }

    @Getter
    @RequiredArgsConstructor
    private enum BlockDrops {
        STONE(Material.STONE, Material.COBBLESTONE, 0, false),
        IRON_ORE(Material.IRON_ORE, Material.IRON_INGOT, 4, true),
        GOLD_ORE(Material.GOLD_ORE, Material.GOLD_INGOT, 4, true),
        ;

        private final Material from;
        private final Material to;
        private final int xp;
        private final boolean oreMagnetAvailable;
    }

    public boolean isOreMagnetAvailable(Material from){
        for (BlockDrops drops : BlockDrops.values()) {
            if (drops.getFrom().equals(from)) {
                return drops.isOreMagnetAvailable();
            }
        }

        return false;
    }

    public Material to(Material from) {
        for (BlockDrops drops : BlockDrops.values()) {
            if (drops.getFrom().equals(from)) {
                return drops.getTo();
            }
        }

        return null;
    }

    public static int xp(Material from) {
        for (BlockDrops drops : BlockDrops.values()) {
            if (drops.getFrom().equals(from)) {
                return drops.getXp();
            }
        }

        return 0;
    }
}
