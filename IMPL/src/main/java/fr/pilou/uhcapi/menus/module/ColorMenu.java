package fr.pilou.uhcapi.menus.module;

import com.google.common.collect.Lists;
import fr.pilou.menuapi.buttons.Button;
import fr.pilou.menuapi.menu.impl.PaginatedMenu;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.player.IGamePlayer;
import fr.pilou.uhcapi.utils.Color;
import fr.pilou.uhcapi.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ColorMenu extends PaginatedMenu {
    private final List<Player> targetList;

    @Override
    public int getGlassColor() {
        return 4;
    }

    @Override
    public String getTitle(Player player) {
        return "§f» §e§lCouleurs";
    }

    @Override
    public List<Button> getPaginatedButtons(Player player) {
        List<Button> buttons = Lists.newArrayList();
        for (Color color : Color.values()) {
            buttons.add(new Button() {
                @Override
                public ItemStack getIcon(Player player) {
                    return new ItemBuilder(color.getIcon())
                            .setName("§f» " + color.getColor() + color.getName())
                            .build();
                }

                @Override
                public void click(Player player, ClickType clickType) {
                    player.closeInventory();
                    IGameHandler gameHandler = API.get().getGameHandler();
                    IGamePlayer gamePlayer = gameHandler.getGamePlayer(player.getUniqueId());
                    for (Player target : targetList) {
                        gamePlayer.setColor(target, color);
                    }
                }
            });
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Map.of();
    }
}