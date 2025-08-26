package fr.pilou.uhcapi.game.world;

import fr.pilou.uhcapi.API;
import org.bukkit.Chunk;
import org.bukkit.block.Biome;

public class UHCGenerator implements BiomeGenerator {

    @Override
    public void swapBiomes(BiomeSwapper swapper) {
        IWorldHandler worldHandler = API.get().getWorldHandler();
        for (Biome biome : Biome.values()) {
            if(biome == worldHandler.getCenterBiome() || biome == worldHandler.getSecondaryBiome()){
                continue;
            }

            swapper.replaceBiome(biome, Biome.FOREST);
        }
    }

    @Override
    public void generateChunk(Chunk chunk) {
        IWorldHandler worldHandler = API.get().getWorldHandler();
        generateCircle(chunk, 0, 0, 600, worldHandler.getSecondaryBiome());
        generateCircle(chunk, 0, 0, 300, worldHandler.getCenterBiome());
    }
}
