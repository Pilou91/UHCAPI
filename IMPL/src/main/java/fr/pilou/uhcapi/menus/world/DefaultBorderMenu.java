package fr.pilou.uhcapi.menus.world;

import com.google.common.collect.Lists;
import fr.pilou.menuapi.buttons.Button;
import fr.pilou.menuapi.menu.impl.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class DefaultBorderMenu extends PaginatedMenu {
    @Override
    public List<Button> getPaginatedButtons(Player player) {
        List<Button> buttons = Lists.newArrayList();
        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return "Configurer la bordure";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Map.of();
    }
}
