package fr.pilou.uhcapi.game.world;

import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.utils.BarUtils;
import fr.pilou.uhcapi.utils.CC;
import fr.pilou.uhcapi.utils.ComponentUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Preload {
    public static boolean finished;
    private static double percentage = 0D;
    private Double currentChunkLoad;
    private final Double totalChunkToLoad;
    private int cx;
    private int cz;
    private final int radius;
    private final World world;
    private long executedAt;

    public Preload() {
        IWorldHandler worldHandler = API.get().getWorldHandler();
        int radius = worldHandler.getWorldBorderSize();
        radius += 150;
        finished = false;
        percentage = 0.0D;
        this.totalChunkToLoad = Math.pow(radius, 2.0D) / 64.0D;
        this.currentChunkLoad = 0.0D;
        this.cx = -radius;
        this.cz = -radius;
        this.radius = radius;
        this.world = worldHandler.getWorld();
        this.executedAt = System.currentTimeMillis();
    }

    public void load() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5 && !finished; i++) {
                    Location loc = new Location(world, cx, 0.0D, cz);
                    Chunk chunk = loc.getChunk();
                    if (!chunk.isLoaded())
                        chunk.load();
                    cx = cx + 16;
                    currentChunkLoad = currentChunkLoad + 1.0D;
                    if (cx > radius) {
                        cx = -radius;
                        cz = cz + 16;
                        if (cz > radius) {
                            currentChunkLoad = totalChunkToLoad;
                            finished = true;
                        }
                    }
                }
                percentage = currentChunkLoad / totalChunkToLoad * 100.0D;
                if (finished) {
                    cancel();
                }
            }
        }.runTaskTimer(API.get().getPlugin(), 0, 1L);
        sendActionBar();
    }


    private void sendActionBar() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (finished) {
                    new Location(world, 0, 100, 0).getChunk().load(true);
                    cancel();
                    return;
                }
                if (percentage > 100) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ComponentUtils.sendActionBar(player, "§f» §ePrégénération §r" + CC.SYMBOL_BAR + " §aTerminé §f«");
                    }
                    return;
                }
                String bar = BarUtils.getBar(percentage, 10);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ComponentUtils.sendActionBar(player, "§f» §ePrégénération §r" + CC.SYMBOL_BAR + " §8(" + bar + "§8) §f«");
                }
            }
        }.runTaskTimer(API.get().getPlugin(), 0, 1L);
    }
}