package fr.pilou.uhcapi.game.world;

import org.bukkit.World;

import java.util.Map;

public interface IBiomeGeneratorHandler {
    Map<String, BiomeGenerator> getBiomeGenerators();

    BiomeSwapper getBiomeSwapper();

    void registerBiomeGenerator(String worldName, BiomeGenerator biomeGenerator);

    BiomeGenerator getBiomeGenerator(World world);

    void unregisterBiomeGenerator(World world);
}
