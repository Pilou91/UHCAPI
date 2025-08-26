package fr.pilou.uhcapi.items.impl;

import fr.pilou.uhcapi.items.ClickableItem;
import fr.pilou.uhcapi.menus.MainMenu;
import fr.pilou.uhcapi.utils.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MainClickableItem extends ClickableItem {
    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.CHEST);
    }

    @Override
    public String getName() {
        return "Configuration";
    }


    @Override
    public List<String> getLore() {
        return List.of("§8" + CC.SYMBOL_BAR + " §rPermet d'ouvrir le §amenu §rde configuration de la §epartie§r.");
    }

    @Override
    public void onClick(Player player, Action action) {
        new MainMenu().open(player);
    }
}
