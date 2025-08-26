package fr.pilou.uhcapi.listeners;

import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.world.BiomeGenerator;
import fr.pilou.uhcapi.game.world.BiomeSwapper;
import fr.pilou.uhcapi.game.world.IBiomeGeneratorHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

@ListenerRegistry()
public class BiomeGeneratorListener implements Listener {
    private final IBiomeGeneratorHandler biomeGeneratorManager = API.get().getBiomeGeneratorHandler();

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (!event.isNewChunk()) return;
        BiomeGenerator generator = biomeGeneratorManager.getBiomeGenerator(event.getWorld());
        if (generator == null) return;

        BiomeSwapper biomeSwapper = biomeGeneratorManager.getBiomeSwapper();

        biomeSwapper.resetBiomes();
        generator.swapBiomes(biomeSwapper);
        generator.generateChunk(event.getChunk());
    }
}
