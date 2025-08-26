package fr.pilou.uhcapi.game.world;

import org.bukkit.Chunk;
import org.bukkit.block.Biome;

public interface BiomeGenerator {

    /**
     * Swaps the biomes in the given BiomeGenerator using the provided BiomeSwapper.
     *
     * @param swapper The BiomeSwapper used to perform the biome swaps.
     */
    default void swapBiomes(BiomeSwapper swapper) {
    }

    /**
     * Generates a chunk with a specific biome.
     *
     * @param chunk The chunk to generate.
     */
    default void generateChunk(Chunk chunk) {
    }

    /**
     * Generates a circular area of a given radius with a specified biome in a given chunk.
     *
     * @param chunk   the chunk in which to generate the circle
     * @param centerX the x-coordinate of the center of the circle
     * @param centerZ the z-coordinate of the center of the circle
     * @param radius  the radius of the circle
     * @param biome   the biome to set in the generated area
     */
    default void generateCircle(Chunk chunk, int centerX, int centerZ, int radius, Biome biome) {
        int chunkX = chunk.getX() * 16;
        int chunkZ = chunk.getZ() * 16;

        for (int x = chunkX; x < chunkX + 16; x++) {
            for (int z = chunkZ; z < chunkZ + 16; z++) {
                if (Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerZ - z, 2)) <= radius) {
                    chunk.getBlock(x, 0, z).setBiome(biome);
                }
            }
        }
    }
}
