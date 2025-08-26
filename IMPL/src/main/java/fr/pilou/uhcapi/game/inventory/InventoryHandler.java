package fr.pilou.uhcapi.game.inventory;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@Getter
public class InventoryHandler implements IInventoryHandler {
    private final ItemStack[] contents = new ItemStack[36];
    private final ItemStack[] armorContents = new ItemStack[4];

    @Override
    public void saveInventory(PlayerInventory playerInventory) {
        System.arraycopy(playerInventory.getContents(), 0, contents, 0, contents.length);
        System.arraycopy(playerInventory.getArmorContents(), 0, armorContents, 0, armorContents.length);
    }

    @Override
    public void applyInventory(PlayerInventory playerInventory) {
        playerInventory.setContents(contents);
        playerInventory.setArmorContents(armorContents);
    }
}
