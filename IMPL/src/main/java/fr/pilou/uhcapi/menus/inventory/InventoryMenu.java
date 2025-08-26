package fr.pilou.uhcapi.menus.inventory;

import com.google.common.collect.Maps;
import fr.pilou.menuapi.buttons.Button;
import fr.pilou.menuapi.buttons.impl.BackButton;
import fr.pilou.menuapi.menu.Menu;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.inventory.IInventoryHandler;
import fr.pilou.uhcapi.game.player.IGamePlayer;
import fr.pilou.uhcapi.menus.MainMenu;
import fr.pilou.uhcapi.utils.CC;
import fr.pilou.uhcapi.utils.ItemBuilder;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.awt.*;
import java.util.Map;

public class InventoryMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "§f» §e§lInventaire de départ";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        IInventoryHandler inventoryHandler = API.get().getInventoryHandler();
        int[] glassPaneSlots = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 50, 51, 52};
        for (int slot : glassPaneSlots) {
            buttons.put(slot, new Button() {
                @Override
                public ItemStack getIcon(Player player) {
                    return new ItemBuilder(Material.STAINED_GLASS_PANE)
                            .setDurability(4)
                            .setName("§r")
                            .build();
                }
            });
        }
        int j = 0;
        for (int slot = 9; slot < 45; slot++) {
            int finalJ = j;
            ItemStack content = inventoryHandler.getContents()[finalJ];
            j++;

            if(content == null || content.getType() == Material.AIR){
                continue;
            }

            buttons.put(slot, new Button() {
                @Override
                public ItemStack getIcon(Player player) {
                    return content;
                }
            });
        }
        int i = 0;
        for (int slot = 45; slot < 49; slot++) {
            int finalI = i;
            ItemStack armorContent = inventoryHandler.getArmorContents()[finalI];
            i++;

            if(armorContent == null || armorContent.getType() == Material.AIR){
                continue;
            }

            buttons.put(slot, new Button() {
                @Override
                public ItemStack getIcon(Player player) {
                    return armorContent;
                }
            });
        }
        buttons.put(53, new Button() {
            @Override
            public ItemStack getIcon(Player player) {
                return new ItemBuilder(Material.SLIME_BALL)
                        .setName("§f» §eModifier l'inventaire de départ")
                        .build();
            }

            @Override
            public void click(Player player, ClickType clickType) {
                PlayerInventory inventory = player.getInventory();
                inventory.clear();
                inventory.clear();
                inventory.setHelmet(null);
                inventory.setChestplate(null);
                inventory.setLeggings(null);
                inventory.setBoots(null);
                player.setGameMode(GameMode.CREATIVE);

                IGameHandler gameHandler = API.get().getGameHandler();
                IGamePlayer gamePlayer = gameHandler.getGamePlayer(player.getUniqueId());
                gamePlayer.setInventoryEdit(true);

                inventoryHandler.applyInventory(inventory);

                TextComponent textComponent = new TextComponent(CC.uhcPrefix("Lorsque vous avez fini de modifier l'§einventaire de départ§r, " +
                        "utiliser la commande §c/h finish §rou cliquer sur le §amessage§r."));
                textComponent.setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§f» §eClique ici").create()
                ));
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/h finish"));
                player.spigot().sendMessage(textComponent);

                player.closeInventory();
            }
        });
        buttons.put(49, new BackButton(new MainMenu()));
        return buttons;
    }
}
