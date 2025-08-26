package fr.pilou.uhcapi.game.world;

import org.bukkit.World;
import org.bukkit.block.Biome;

public interface IWorldHandler {
    World getWorld();

    void setWorld(World world);

    int getWorldBorderSize();

    void setWorldBorderSize(int size);

    Biome getCenterBiome();

    void setCenterBiome(Biome biome);

    Biome getSecondaryBiome();

    void setSecondaryBiome(Biome biome);

    void createWorld();

    void deleteWorld();
}
