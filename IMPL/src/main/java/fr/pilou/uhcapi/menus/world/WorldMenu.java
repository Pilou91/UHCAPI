package fr.pilou.uhcapi.menus.world;

import fr.pilou.menuapi.buttons.Button;
import fr.pilou.menuapi.buttons.impl.BackButton;
import fr.pilou.menuapi.menu.Menu;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.world.IWorldHandler;
import fr.pilou.uhcapi.game.world.Preload;
import fr.pilou.uhcapi.menus.MainMenu;
import fr.pilou.uhcapi.utils.BiomeUtils;
import fr.pilou.uhcapi.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class WorldMenu extends Menu {
    private final IWorldHandler worldHandler = API.get().getWorldHandler();

    @Override
    public String getTitle(Player player) {
        return "§f» §e§lGestion du Monde";
    }

    @Override
    public int getGlassColor() {
        return 4;
    }

    @Override
    public int getSize() {
        return 6 * 9;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Map.of(
                11, new Button() {
                    @Override
                    public ItemStack getIcon(Player player) {
                        Biome centerBiome = worldHandler.getCenterBiome();
                        BiomeUtils.BiomeInfo biomeInfo = BiomeUtils.getBiomeInfo(centerBiome);
                        return new ItemBuilder(biomeInfo.getItemStack())
                                .setName("§f» §e§lBiome Central : §r"
                                + biomeInfo.getName())
                                .build();
                    }

                    @Override
                    public void click(Player player, ClickType clickType) {
                        new ConfigBiomeMenu(true).open(player);
                    }
                },
                12, new Button() {
                    @Override
                    public ItemStack getIcon(Player player) {
                        Biome secondaryBiome = worldHandler.getSecondaryBiome();
                        BiomeUtils.BiomeInfo biomeInfo = BiomeUtils.getBiomeInfo(secondaryBiome);
                        return new ItemBuilder(biomeInfo.getItemStack())
                                .setName("§f» §e§lBiome Secondaire : §r"
                                + biomeInfo.getName())
                                .build();
                    }

                    @Override
                    public void click(Player player, ClickType clickType) {
                        new ConfigBiomeMenu(false).open(player);
                    }
                },
                13, new Button() {
                    @Override
                    public ItemStack getIcon(Player player) {
                        return new ItemBuilder(Material.SLIME_BALL)
                                .setName("§f» §e§lCréer le monde")
                                .build();
                    }

                    @Override
                    public void click(Player player, ClickType clickType) {
                        worldHandler.createWorld();
                    }
                },
                14, new Button() {
                    @Override
                    public ItemStack getIcon(Player player) {
                        return new ItemBuilder(Material.PAINTING)
                                .setName("§f» §e§lPrégénération")
                                .build();
                    }

                    @Override
                    public void click(Player player, ClickType clickType) {
                        new Preload().load();
                    }
                },
                15, new Button() {
                    @Override
                    public ItemStack getIcon(Player player) {
                        return new ItemBuilder(Material.EYE_OF_ENDER)
                                .setName("§f» §e§lVoir le monde")
                                .build();
                    }

                    @Override
                    public void click(Player player, ClickType clickType) {
                        World world = worldHandler.getWorld();
                        player.teleport(world.getSpawnLocation());
                    }
                }, 31, new Button() {
                    @Override
                    public ItemStack getIcon(Player player) {
                        return new ItemBuilder(Material.BARRIER)
                                .setName("§f» §e§lGestion de la bordure")
                                .build();
                    }

                    @Override
                    public void click(Player player, ClickType clickType) {
                        new DefaultBorderMenu().open(player);
                    }
                },
                49, new BackButton(new MainMenu())
        );
    }
}
