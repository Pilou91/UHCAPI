package fr.pilou.uhcapi.game.inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public interface IInventoryHandler {
    ItemStack[] getContents();

    ItemStack[] getArmorContents();

    void saveInventory(PlayerInventory playerInventory);

    void applyInventory(PlayerInventory playerInventory);
}
