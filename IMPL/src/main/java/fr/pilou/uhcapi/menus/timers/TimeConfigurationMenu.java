package fr.pilou.uhcapi.menus.timers;

import com.google.common.collect.Maps;
import fr.pilou.menuapi.buttons.Button;
import fr.pilou.menuapi.buttons.impl.BackButton;
import fr.pilou.menuapi.menu.Menu;
import fr.pilou.uhcapi.game.timers.Timer;
import fr.pilou.uhcapi.utils.CC;
import fr.pilou.uhcapi.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@RequiredArgsConstructor
public class TimeConfigurationMenu extends Menu {
    private final Timer timer;

    @Override
    public int getGlassColor() {
        return 4;
    }

    @Override
    public int getSize() {
        return 4 * 9;
    }

    @Override
    public String getTitle(Player player) {
        return "§f» §e§lConfiguration du temps";
    }

    @RequiredArgsConstructor
    public class TimeButton extends Button {
        private final boolean add;
        private final int amount;

        public ItemStack getBanner() {
            int color = add ? (amount < 60 ? 10 : 2) : (amount < 60 ? 14 : 1);
            return new ItemBuilder(Material.BANNER)
                    .setDurability(color)
                    .build();
        }

        @Override
        public ItemStack getIcon(Player player) {
            return new ItemBuilder(getBanner())
                    .setName("§f» §e" + (add ? "Ajouter" : "Enlever") + " " + (amount == 60 ? "1min" : amount + "s"))
                    .build();
        }

        @Override
        public void click(Player player, ClickType clickType) {
            int toAdd = timer.getActivationTime() + (add ? amount : -amount);
            toAdd = toAdd <= 0 ? 10 : toAdd;
            timer.setActivationTime(toAdd);
        }

        @Override
        public boolean update(Player player, ClickType clickType) {
            return true;
        }
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(11, new TimeButton(false, 60));
        buttons.put(12, new TimeButton(false, 10));
        buttons.put(13, new Button() {
            @Override
            public ItemStack getIcon(Player player) {
                return new ItemBuilder(timer.getIcon())
                        .setName("§f» §e" + timer.getName() + " §f(§e" + CC.timeWithLetter(timer.getActivationTime()) + "§f)")
                        .addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES,
                                ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON,
                                ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS)
                        .build();
            }
        });
        buttons.put(14, new TimeButton(true, 10));
        buttons.put(15, new TimeButton(true, 60));
        buttons.put(31, new BackButton(new TimersMenu()));
        return buttons;
    }
}
