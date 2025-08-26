package fr.pilou.uhcapi.game.world;

import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.utils.CC;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;

@Data
public class WorldHandler implements IWorldHandler{
    private World world;
    private int worldBorderSize = 100;
    private Biome centerBiome = Biome.ROOFED_FOREST;
    private Biome secondaryBiome = Biome.MEGA_TAIGA_HILLS;

    @Override
    public void createWorld() {
        Bukkit.broadcastMessage(CC.uhcPrefix("§eCréation §rdu monde en cours..."));
        IBiomeGeneratorHandler biomeManager = API.get().getBiomeGeneratorHandler();
        biomeManager.registerBiomeGenerator("uhc-world", new UHCGenerator());
        WorldCreator creator = WorldCreator.name("uhc-world");
        World newWorld = creator.createWorld();
        WorldBorder worldBorder = newWorld.getWorldBorder();
        worldBorder.setSize(getWorldBorderSize() * 2);

        newWorld.setGameRuleValue("naturalRegeneration", "false");
        setWorld(newWorld);

        Bukkit.broadcastMessage(CC.uhcPrefix("§eCréation §rdu monde terminé avec §asuccès§r."));
    }

    @Override
    public void deleteWorld() {
        if (world != null) {
            String worldName = world.getName();
            org.bukkit.Bukkit.unloadWorld(world, false);

            java.io.File worldFolder = new java.io.File(org.bukkit.Bukkit.getWorldContainer(), worldName);
            deleteFolder(worldFolder);
        }
    }

    private void deleteFolder(java.io.File folder) {
        if (folder.isDirectory()) {
            for (java.io.File file : folder.listFiles()) {
                deleteFolder(file);
            }
        }
        folder.delete();
    }
}
