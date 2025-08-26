package fr.pilou.uhcapi.game.world;


import lombok.Getter;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BiomeGeneratorHandler implements IBiomeGeneratorHandler{

    /**
     * The `biomeGenerators` variable is a private final Map that holds String keys and BiomeGenerator values.
     * It is a member variable of the `BiomeGeneratorHandler` class.
     * <p>
     * This map is used to store and manage different biome generators for different worlds in the system.
     * The keys in the map represent the world names, and the values represent the corresponding biome generators
     * for each world.
     * <p>
     * BiomeGenerator is an interface that defines methods for swapping biomes, generating chunks, and generating
     * circular areas with specified biomes. The BiomeGeneratorHandler class uses this map to store the biome
     * generators for each world and provides methods to register, unregister, and retrieve the biome generators
     * for a given world.
     * <p>
     * The BiomeSwapper interface defines methods for replacing and swapping biomes, and the BiomeSwapperImpl
     * class provides the implementation of these methods. The BiomeGeneratorHandler class uses an instance of
     * BiomeSwapperImpl to perform the swapping operations on the biome generators.
     * <p>
     * Note: This documentation does not include example code or author/version tags, as per the specification.
     */
    private final Map<String, BiomeGenerator> biomeGenerators;
    /**
     * Represents a biome swapper, used to replace or swap biomes in a world.
     * BiomeSwapper provides methods to replace a specific biome with a replacement biome,
     * as well as swapping the values of two biomes.
     */
    private final BiomeSwapper biomeSwapper;

    /**
     * The BiomeGeneratorHandler class is responsible for managing and organizing BiomeGenerators.
     * It provides methods for registering, retrieving, and unregistering BiomeGenerators.
     */
    public BiomeGeneratorHandler() {
        this.biomeGenerators = new HashMap<>();
        this.biomeSwapper = new BiomeSwapper();
    }

    /**
     * Registers a BiomeGenerator for a specific world.
     *
     * @param worldName      the name of the world
     * @param biomeGenerator the BiomeGenerator to register
     */
    public void registerBiomeGenerator(String worldName, BiomeGenerator biomeGenerator) {
        this.biomeGenerators.put(worldName, biomeGenerator);
    }

    /**
     * Retrieves the biome generator for the specified world.
     *
     * @param world The world for which to retrieve the biome generator.
     * @return The biome generator for the specified world.
     */
    public BiomeGenerator getBiomeGenerator(World world) {
        return this.biomeGenerators.get(world.getName());
    }

    /**
     * Unregisters a BiomeGenerator for the specified world.
     *
     * @param world The world for which to unregister the biome generator.
     */
    public void unregisterBiomeGenerator(World world) {
        this.biomeGenerators.remove(world.getName());
    }

}
