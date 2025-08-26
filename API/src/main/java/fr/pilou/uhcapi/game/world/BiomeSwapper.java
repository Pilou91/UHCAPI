package fr.pilou.uhcapi.game.world;

import net.minecraft.server.v1_8_R3.BiomeBase;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;

public class BiomeSwapper {
    /**
     * An array of default biomes.
     */
    private static final BiomeBase[] defaultBiomes;

    static {
        defaultBiomes = BiomeBase.getBiomes().clone();
    }


    /**
     * Replaces a specific biome with the specified replacement biome.
     *
     * @param overwritten      The biome to be replaced.
     * @param replacementBiome The biome to replace the overwritten biome with.
     */
    public synchronized void replaceBiome(final Biome overwritten, final Biome replacementBiome) {
        BiomeBase[] biomes = BiomeBase.getBiomes();
        BiomeBase firstBiome = CraftBlock.biomeToBiomeBase(overwritten);
        BiomeBase secondBiome = CraftBlock.biomeToBiomeBase(replacementBiome);

        for (int i = 0; i < biomes.length; i++) {
            if (biomes[i] == firstBiome) {
                biomes[i] = secondBiome;
            }
        }
    }

    /**
     * Swaps the values of two Biome instances in the biome array.
     *
     * @param first  the first Biome instance to swap
     * @param second the second Biome instance to swap
     */
    public synchronized void swapBiome(final Biome first, final Biome second) {
        BiomeBase[] biomes = BiomeBase.getBiomes();
        BiomeBase firstBiome = CraftBlock.biomeToBiomeBase(first);
        BiomeBase secondBiome = CraftBlock.biomeToBiomeBase(second);

        for (int i = 0; i < biomes.length; i++) {
            if (biomes[i] == firstBiome) {
                biomes[i] = secondBiome;
            } else if (biomes[i] == secondBiome) {
                biomes[i] = firstBiome;
            }
        }
    }

    /**
     * Reset the biomes to their default values.
     */
    public synchronized void resetBiomes() {
        BiomeBase[] biomes = BiomeBase.getBiomes();
        System.arraycopy(defaultBiomes, 0, biomes, 0, biomes.length);
    }
}
