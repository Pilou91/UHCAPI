package fr.pilou.uhcapi.game.scenarios.impl;

import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.scenarios.Scenario;
import fr.pilou.uhcapi.game.scenarios.ScenarioRegistry;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@ScenarioRegistry(
        name = "Timber",
        material = Material.LOG,
        lore = {
                "§rPermet de casser l'§2arbre §ren §centier",
                "§ren ne cassant qu'une §eseule §rbûche."
        }
)
public class TimberScenario extends Scenario implements Listener {
    private final Map<Location, Integer> limitTimber = new HashMap<>();

    @EventHandler
    public void breakingBlock(BlockBreakEvent event) {
        if (event.isCancelled() && !event.getPlayer().getGameMode().equals(GameMode.SURVIVAL))
            return;

        if (!(event.getBlock().getType().name().contains("LOG"))) return;

        limitTimber.put(event.getBlock().getLocation(), 0);
        breakBlock(event.getBlock(), event.getBlock().getLocation(), event.getPlayer());
    }

    public void breakBlock(Block block, Location model, Player player) {
        JavaPlugin main = API.get().getPlugin();

        if (limitTimber.containsKey(model)) {
            int MAX_TIMER = 90;
            if (limitTimber.get(model) >= MAX_TIMER) {
                limitTimber.remove(model);
                return;

            } else limitTimber.put(model, limitTimber.get(model) + 1);
        } else return;

        PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(2001, new BlockPosition(block.getX(), block.getY(), block.getZ()), block.getTypeId(), false);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        block.breakNaturally();

        if (percentChance(5))
            block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.APPLE, 1));

        new BukkitRunnable() {
            public void run() {
                Block above = blockNext(block, 0, 1, 0);
                if (isWoodOrLeaves(above)) {
                    breakBlock(above, model, player);
                }
            }
        }.runTaskLater(main, 1);

        new BukkitRunnable() {
            public void run() {
                Block behind = blockNext(block, 1, 0, 0);
                if (isWoodOrLeaves(behind)) {
                    breakBlock(behind, model, player);
                }
            }
        }.runTaskLater(main, 2);

        new BukkitRunnable() {
            public void run() {
                Block ahead = blockNext(block, -1, 0, 0);
                if (isWoodOrLeaves(ahead)) {
                    breakBlock(ahead, model, player);
                }
            }
        }.runTaskLater(main, 3);

        new BukkitRunnable() {
            public void run() {
                Block left = blockNext(block, 0, 0, 1);
                if (isWoodOrLeaves(left)) {
                    breakBlock(left, model, player);
                }
            }
        }.runTaskLater(main, 4);

        new BukkitRunnable() {
            public void run() {
                Block right = blockNext(block, 0, 0, -1);
                if (isWoodOrLeaves(right)) {
                    breakBlock(right, model, player);
                }
            }
        }.runTaskLater(main, 5);

        new BukkitRunnable() {
            public void run() {
                Block below = blockNext(block, 0, -1, 0);
                if (isWoodOrLeaves(below)) {
                    breakBlock(below, model, player);
                }
            }
        }.runTaskLater(main, 6);
    }

    public boolean isWoodOrLeaves(Block block) {
        Material material = block.getType();
        return material.name().contains("LOG");
    }

    public Block blockNext(Block block, int x, int y, int z) {
        Location location = block.getLocation();
        Location above = new Location(block.getWorld(), location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
        return above.getBlock();
    }

    private int gRi() {
        return ThreadLocalRandom.current().nextInt(100) + 1;
    }

    public boolean percentChance(int zeroAcent) {
        return gRi() <= zeroAcent;
    }
}
