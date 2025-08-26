package fr.pilou.uhcapi.utils;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class BiomeUtils {
    private static final Map<Biome, BiomeInfo> biomesMap = Maps.newHashMap();

    public static BiomeInfo getBiomeInfo(Biome biome){
        return biomesMap.get(biome);
    }

    @Getter
    @RequiredArgsConstructor
    public static class BiomeInfo{
        private final String name;
        private final ItemStack itemStack;
    }

    public static void init() {
        biomesMap.put(Biome.PLAINS,
                new BiomeInfo("Plaine", new ItemStack(Material.LONG_GRASS, 1, (byte) 1)));

        biomesMap.put(Biome.ROOFED_FOREST,
                new BiomeInfo("Forêt Sombre", new ItemStack(Material.SAPLING, 1, (byte) 5)));

        biomesMap.put(Biome.FOREST,
                new BiomeInfo("Forêt", new ItemStack(Material.SAPLING)));

        biomesMap.put(Biome.BIRCH_FOREST,
                new BiomeInfo("Forêt de Bouleaux", new ItemStack(Material.SAPLING, 1, (byte) 2)));

        biomesMap.put(Biome.JUNGLE,
                new BiomeInfo("Jungle", new ItemStack(Material.SAPLING, 1, (byte) 3)));

        biomesMap.put(Biome.MEGA_TAIGA,
                new BiomeInfo("Taïga Géante", new ItemStack(Material.SAPLING, 1, (byte) 1)));

        biomesMap.put(Biome.MEGA_TAIGA_HILLS,
                new BiomeInfo("Taïga Géante (Collines)", new ItemStack(Material.SAPLING, 1, (byte) 1)));

        biomesMap.put(Biome.TAIGA,
                new BiomeInfo("Taïga", new ItemStack(Material.SAPLING, 1, (byte) 1)));

        biomesMap.put(Biome.EXTREME_HILLS,
                new BiomeInfo("Hautes Collines", new ItemStack(Material.STONE)));
    }
}
