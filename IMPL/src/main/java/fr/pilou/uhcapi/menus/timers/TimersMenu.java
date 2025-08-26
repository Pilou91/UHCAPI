package fr.pilou.uhcapi.menus.timers;

import com.google.common.collect.Lists;
import fr.pilou.menuapi.buttons.Button;
import fr.pilou.menuapi.buttons.impl.BackButton;
import fr.pilou.menuapi.menu.impl.PaginatedMenu;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.timers.ITimerHandler;
import fr.pilou.uhcapi.game.timers.Timer;
import fr.pilou.uhcapi.menus.MainMenu;
import fr.pilou.uhcapi.utils.CC;
import fr.pilou.uhcapi.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class TimersMenu extends PaginatedMenu {
    @Override
    public String getTitle(Player player) {
        return "§f» §e§lTimers";
    }

    @Override
    public int getGlassColor() {
        return 4;
    }

    @Override
    public List<Button> getPaginatedButtons(Player player) {
        List<Button> buttons = Lists.newArrayList();
        ITimerHandler timerHandler = API.get().getTimerHandler();
        for (Timer timer : timerHandler.getTimers()) {
            if(timer.getIcon() == null){
                continue;
            }

            buttons.add(new Button() {
                @Override
                public ItemStack getIcon(Player player) {
                    return new ItemBuilder(timer.getIcon())
                            .setName("§f» §e" + timer.getName() + " §f(§e" + CC.timeWithLetter(timer.getActivationTime()) + "§f)")
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES,
                                    ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON,
                                    ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS)
                            .build();
                }

                @Override
                public void click(Player player, ClickType clickType) {
                    new TimeConfigurationMenu(timer).open(player);
                }
            });
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Map.of(49, new BackButton(new MainMenu()));
    }
}
