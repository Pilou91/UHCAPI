package fr.pilou.uhcapi.items;

import fr.pilou.uhcapi.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class ClickableItem {
    public abstract ItemStack getItem();

    public abstract String getName();

    public abstract List<String> getLore();

    public void onClick(Player player, Action action) {

    }

    public ItemStack getIcon() {
        return new ItemBuilder(getItem())
                .setName("§f» §e§l" + getName())
                .setLore(getLore())
                .build();
    }
}
