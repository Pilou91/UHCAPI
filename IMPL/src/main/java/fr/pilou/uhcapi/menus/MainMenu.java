package fr.pilou.uhcapi.menus;

import fr.pilou.menuapi.buttons.Button;
import fr.pilou.menuapi.menu.Menu;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.State;
import fr.pilou.uhcapi.game.world.IWorldHandler;
import fr.pilou.uhcapi.menus.inventory.InventoryMenu;
import fr.pilou.uhcapi.menus.module.CompositionMenu;
import fr.pilou.uhcapi.menus.scenarios.ScenariosMenu;
import fr.pilou.uhcapi.menus.timers.TimersMenu;
import fr.pilou.uhcapi.menus.world.WorldMenu;
import fr.pilou.uhcapi.tasks.StartingRunnable;
import fr.pilou.uhcapi.utils.CC;
import fr.pilou.uhcapi.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class MainMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "§f» §e§lConfiguration";
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }

    @Override
    public int getGlassColor() {
        return 4;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Map.of(40, new Button() {
                    @Override
                    public ItemStack getIcon(Player player) {
                        IGameHandler gameHandler = API.get().getGameHandler();
                        State state = gameHandler.getState();
                        return new ItemBuilder(Material.INK_SACK)
                                .setDurability(state == State.WAITING ? 10 : 1)
                                .setName("§f» §e§l" + (state == State.WAITING ? "Démarrer" : "Arrêter") + " la partie")
                                .build();
                    }

                    @Override
                    public void click(Player player, ClickType clickType) {
                        IGameHandler gameHandler = API.get().getGameHandler();
                        if (gameHandler.getState() == State.IN_GAME) {
                            return;
                        }

                        if (gameHandler.getState() == State.WAITING) {
                            IWorldHandler worldHandler = API.get().getWorldHandler();
                            if(worldHandler.getWorld() == null){
                                player.sendMessage(CC.uhcPrefix("§cVous devez d'abord créer le monde."));
                                return;
                            }

                            gameHandler.setState(State.STARTING);
                            new StartingRunnable().runTaskTimer(API.get().getPlugin(), 0L, 20L);
                            player.closeInventory();
                            return;
                        }

                        gameHandler.setState(State.WAITING);
                    }

                    @Override
                    public boolean update(Player player, ClickType clickType) {
                        return true;
                    }
                }, 12, new Button() {
                    @Override
                    public ItemStack getIcon(Player player) {
                        return new ItemBuilder(Material.SEEDS)
                                .setName("§f» §e§lGestion du Monde")
                                .build();
                    }

                    @Override
                    public void click(Player player, ClickType clickType) {
                        new WorldMenu().open(player);
                    }
                }, 14, new Button() {
                    @Override
                    public ItemStack getIcon(Player player) {
                        return new ItemBuilder(Material.BOOK)
                                .setName("§f» §e§lScenarios")
                                .build();
                    }

                    @Override
                    public void click(Player player, ClickType clickType) {
                        new ScenariosMenu().open(player);
                    }
                }, 22, new Button() {
                    @Override
                    public ItemStack getIcon(Player player) {
                        return new ItemBuilder(Material.WATCH)
                                .setName("§f» §e§lTimers")
                                .build();
                    }

                    @Override
                    public void click(Player player, ClickType clickType) {
                        new TimersMenu().open(player);
                    }
                }, 20, new Button() {
                    @Override
                    public ItemStack getIcon(Player player) {
                        return new ItemBuilder(Material.ENDER_CHEST)
                                .setName("§f» §e§lInventaire")
                                .build();
                    }

                    @Override
                    public void click(Player player, ClickType clickType) {
                        new InventoryMenu().open(player);
                    }
                }, 24, new Button() {
                    @Override
                    public ItemStack getIcon(Player player) {
                        return new ItemBuilder(Material.PRISMARINE_SHARD)
                                .setName("§f» §e§lModule")
                                .build();
                    }

                    @Override
                    public void click(Player player, ClickType clickType) {
                        new CompositionMenu().open(player);
                    }
                }
        );
    }
}
