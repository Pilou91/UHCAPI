package fr.pilou.uhcapi.commands;

import fr.pilou.commandapi.commands.ArgsType;
import fr.pilou.commandapi.commands.MainCommand;
import fr.pilou.commandapi.commands.SubCommand;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.inventory.IInventoryHandler;
import fr.pilou.uhcapi.game.player.IGamePlayer;
import fr.pilou.uhcapi.menus.inventory.InventoryMenu;
import fr.pilou.uhcapi.menus.MainMenu;
import fr.pilou.uhcapi.utils.CC;
import fr.pilou.uhcapi.utils.ComponentUtils;
import fr.pilou.uhcapi.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class HostCommand extends MainCommand {
    @Override
    public String getCommandName() {
        return "Host";
    }

    @Override
    public List<String> getNames() {
        return List.of("h", "host");
    }

    @Override
    public String getLore() {
        return "";
    }

    @Override
    public List<SubCommand> getSubCommands() {
        return List.of(
                new SubCommand() {
                    @Override
                    public String getName() {
                        return "config";
                    }

                    @Override
                    public String getLore() {
                        return "";
                    }

                    @Override
                    public boolean onSubCommand(Player player, Object object) {
                        new MainMenu().open(player);
                        return true;
                    }
                },
                new SubCommand() {
                    @Override
                    public String getName() {
                        return "finish";
                    }

                    @Override
                    public String getLore() {
                        return "";
                    }

                    @Override
                    public boolean onSubCommand(Player player, Object args) {
                        IGameHandler gameHandler = API.get().getGameHandler();
                        IGamePlayer gamePlayer = gameHandler.getGamePlayer(player.getUniqueId());

                        if (!gamePlayer.isInventoryEdit()) {
                            player.sendMessage(CC.uhcPrefix("§cVous n'êtes pas en train de modifier l'inventaire de départ."));
                            return true;
                        }

                        IInventoryHandler inventoryHandler = API.get().getInventoryHandler();
                        PlayerInventory inventory = player.getInventory();
                        inventoryHandler.saveInventory(inventory);
                        inventory.clear();
                        inventory.setHelmet(null);
                        inventory.setChestplate(null);
                        inventory.setLeggings(null);
                        inventory.setBoots(null);

                        player.setGameMode(GameMode.SURVIVAL);

                        gamePlayer.loadHotBar();
                        gamePlayer.setInventoryEdit(false);

                        new InventoryMenu().open(player);
                        return true;
                    }
                },
                new SubCommand() {
                    @Override
                    public String getName() {
                        return "heal";
                    }

                    @Override
                    public String getLore() {
                        return "";
                    }

                    @Override
                    public boolean onSubCommand(Player player, Object args) {
                        Bukkit.broadcastMessage(CC.uhcPrefix("Tous les joueurs viennent d'être §dsoigné§r."));

                        IGameHandler gameHandler = API.get().getGameHandler();
                        for (IGamePlayer gamePlayer : gameHandler.getGamePlayers()) {
                            Player target = gamePlayer.getPlayer();
                            if (target == null) {
                                continue;
                            }

                            target.setHealth(target.getMaxHealth());
                        }
                        return true;
                    }
                }, new SubCommand() {
                    @Override
                    public String getName() {
                        return "setgroup";
                    }

                    @Override
                    public String getLore() {
                        return "";
                    }

                    @Override
                    public ArgsType getType() {
                        return ArgsType.INTEGER;
                    }

                    @Override
                    public boolean onSubCommand(Player player, Object args) {
                        if(!(args instanceof Integer groupSize)){
                            return false;
                        }

                        IGameHandler gameHandler = API.get().getGameHandler();
                        gameHandler.setGroup(groupSize);

                        for (Player target : Bukkit.getOnlinePlayers()) {
                            target.playSound(player.getLocation(), Sound.BLAZE_HIT, 1f, 1f);
                            ComponentUtils.sendTitle(target, "§f» §cGroupes de " + groupSize + " §f«", "§4Respectez la limite des groupes", 20, 20, 20);
                        }
                        return true;
                    }
                },
                new SubCommand() {
                    @Override
                    public String getName() {
                        return "fillwater";
                    }

                    @Override
                    public ArgsType getType() {
                        return ArgsType.INTEGER;
                    }

                    @Override
                    public String getLore() {
                        return "";
                    }

                    @Override
                    public boolean onSubCommand(Player player, Object args) {
                        if (!(args instanceof Integer radius)) {
                            return false;
                        }

                        Location center = player.getLocation();
                        for (int x = -radius; x < radius; x++) {
                            for (int y = -5; y < 5; y++) {
                                for (int z = -radius; z < radius; z++) {
                                    Location location = center.clone().add(x, y, z);
                                    Block block = location.getBlock();
                                    Material type = block.getType();
                                    if (type != Material.WATER && type != Material.STATIONARY_WATER &&
                                            type != Material.SAND && type != Material.GRAVEL) {
                                        continue;
                                    }

                                    block.setType(Material.GRASS);
                                }
                            }
                        }
                        return true;
                    }
                }, new SubCommand() {
                    @Override
                    public String getName() {
                        return "filltree";
                    }

                    @Override
                    public ArgsType getType() {
                        return ArgsType.INTEGER;
                    }

                    @Override
                    public String getLore() {
                        return "";
                    }

                    @Override
                    public boolean onSubCommand(Player player, Object args) {
                        if (!(args instanceof Integer radius)) {
                            return false;
                        }

                        Location center = player.getLocation();
                        World world = player.getWorld();
                        int spacing = 5;

                        for (int x = -radius; x <= radius; x += spacing) {
                            for (int z = -radius; z <= radius; z += spacing) {
                                Location loc = center.clone().add(x, 0, z);
                                loc.setY(world.getHighestBlockYAt(loc));

                                Biome biome = loc.getBlock().getBiome();
                                TreeType treeType = null;

                                if (biome.name().contains("ROOFED_FOREST")) {
                                    double rand = Math.random();
                                    if (rand < 0.9) {
                                        treeType = TreeType.DARK_OAK;
                                    } else {
                                        treeType = TreeType.TREE;
                                    }

                                } else if (biome.name().contains("TAIGA")) {
                                    double rand = Math.random();
                                    if (rand < 0.6) {
                                        treeType = TreeType.MEGA_REDWOOD;
                                    } else {
                                        treeType = TreeType.REDWOOD;
                                    }
                                } else if (biome == Biome.FOREST) {
                                    double rand = Math.random();
                                    if (rand < 0.6) {
                                        treeType = TreeType.TREE;
                                    } else {
                                        treeType = TreeType.BIRCH;
                                    }
                                }


                                if (treeType != null) {
                                    boolean shouldGenerate = true;

                                    if (biome.name().equals("FOREST")) {
                                        shouldGenerate = Math.random() < 0.3;
                                    }

                                    if (shouldGenerate) {
                                        world.generateTree(loc, treeType);
                                    }
                                }
                            }
                        }

                        player.sendMessage("Arbres générés autour de toi !");
                        return true;
                    }

                }, new SubCommand() {
                    @Override
                    public String getName() {
                        return "revive";
                    }

                    @Override
                    public String getLore() {
                        return "";
                    }

                    @Override
                    public ArgsType getType() {
                        return ArgsType.PLAYER;
                    }

                    @Override
                    public boolean onSubCommand(Player player, Object args) {
                        if (!(args instanceof Player target)) {
                            return false;
                        }

                        IGameHandler gameHandler = API.get().getGameHandler();
                        IGamePlayer gamePlayer = gameHandler.getGamePlayer(target.getUniqueId());
                        if (gamePlayer.isAlive()) {
                            player.sendMessage(CC.modulePrefix("§c" + target.getName() + " joueur n'est pas mort."));
                            return true;
                        }

                        Bukkit.broadcastMessage(CC.modulePrefix("Le joueur §e" + target.getName() + " §rvient d'être §arécussité§r."));
                        gamePlayer.revive();
                        return true;
                    }
                }, new SubCommand() {
                    @Override
                    public String getName() {
                        return "template";
                    }

                    @Override
                    public String getLore() {
                        return "";
                    }

                    @Override
                    public boolean onSubCommand(Player player, Object args) {
                        ItemStack[] startArmor = new ItemStack[4];
                        ItemStack[] startInventory = new ItemStack[36];
                        player.getInventory().clear();

                        startArmor[3] = new ItemBuilder(Material.DIAMOND_HELMET)
                                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                                .setUnbreakable()
                                .build();

                        startArmor[2] = new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                                .setUnbreakable()
                                .build();

                        startArmor[1] = new ItemBuilder(Material.IRON_LEGGINGS)
                                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                                .setUnbreakable()
                                .build();

                        startArmor[0] = new ItemBuilder(Material.DIAMOND_BOOTS)
                                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                                .setUnbreakable()
                                .build();

                        startInventory[0] = new ItemBuilder(Material.DIAMOND_SWORD)
                                .addEnchant(Enchantment.DAMAGE_ALL, 3)
                                .setUnbreakable()
                                .build();

                        startInventory[1] = new ItemBuilder(Material.BOW)
                                .addEnchant(Enchantment.ARROW_DAMAGE, 2)
                                .setUnbreakable()
                                .build();

                        startInventory[2] = new ItemBuilder(Material.DIAMOND_PICKAXE)
                                .addEnchant(Enchantment.DIG_SPEED, 3)
                                .setUnbreakable()
                                .build();

                        startInventory[3] = new ItemBuilder(Material.GOLDEN_APPLE)
                                .setAmount(16)
                                .build();

                        startInventory[4] = new ItemBuilder(Material.WOOD)
                                .setAmount(64)
                                .build();

                        startInventory[5] = new ItemBuilder(Material.LAVA_BUCKET)
                                .build();

                        startInventory[6] = new ItemBuilder(Material.WOOD)
                                .setAmount(64)
                                .build();

                        startInventory[7] = new ItemBuilder(Material.WATER_BUCKET)
                                .build();

                        startInventory[8] = new ItemBuilder(Material.GOLDEN_CARROT)
                                .setAmount(64)
                                .build();

                        startInventory[20] = new ItemBuilder(Material.DIAMOND_SPADE)
                                .addEnchant(Enchantment.DIG_SPEED, 3)
                                .setUnbreakable()
                                .build();

                        startInventory[29] = new ItemBuilder(Material.DIAMOND_AXE)
                                .addEnchant(Enchantment.DIG_SPEED, 3)
                                .setUnbreakable()
                                .build();

                        startInventory[31] = new ItemBuilder(Material.WOOD)
                                .setAmount(64)
                                .build();

                        startInventory[32] = new ItemBuilder(Material.LAVA_BUCKET)
                                .build();

                        startInventory[33] = new ItemBuilder(Material.WOOD)
                                .setAmount(64)
                                .build();

                        startInventory[34] = new ItemBuilder(Material.WATER_BUCKET)
                                .build();

                        startInventory[35] = new ItemBuilder(Material.ARROW)
                                .setAmount(32)
                                .build();

                        startInventory[14] = new ItemBuilder(Material.LAVA_BUCKET)
                                .build();

                        startInventory[23] = new ItemBuilder(Material.LAVA_BUCKET)
                                .build();

                        startInventory[16] = new ItemBuilder(Material.WATER_BUCKET)
                                .build();

                        startInventory[25] = new ItemBuilder(Material.WATER_BUCKET)
                                .build();

                        player.getInventory().setArmorContents(startArmor);
                        player.getInventory().setContents(startInventory);
                        player.updateInventory();
                        return true;
                    }
                }
        );
    }

    @Override
    public void onMainCommand(Player player, String[] args) {
    }
}
