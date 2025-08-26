package fr.pilou.uhcapi.listeners;

import fr.pilou.uhcapi.game.State;
import fr.pilou.uhcapi.items.ClickableItems;
import fr.pilou.uhcapi.items.ClickableItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@ListenerRegistry()
public class ClickableItemsListener implements Listener {
    @EventHandler
    public void onClickableItems(PlayerInteractEvent event){
        ItemStack item = event.getItem();
        for (ClickableItems clickableItems : ClickableItems.values()) {
            fr.pilou.uhcapi.items.ClickableItem clickableItem = clickableItems.getClickableItem();
            if (clickableItem.getIcon().isSimilar(item)) {
                clickableItem.onClick(event.getPlayer(), event.getAction());
                break;
            }
        }
    }

    @EventHandler
    public void onMoveClickableItems(InventoryClickEvent event){
        ItemStack item = event.getCurrentItem();
        for (ClickableItems clickableItems : ClickableItems.values()) {
            fr.pilou.uhcapi.items.ClickableItem clickableItem = clickableItems.getClickableItem();
            if (clickableItem.getIcon().isSimilar(item)) {
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onDropClickableItems(PlayerDropItemEvent event){
        ItemStack item = event.getItemDrop().getItemStack();
        for (ClickableItems clickableItems : ClickableItems.values()) {
            ClickableItem clickableItem = clickableItems.getClickableItem();
            if (clickableItem.getIcon().isSimilar(item)) {
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onPlaceClickableItems(BlockPlaceEvent event){
        ItemStack item = event.getItemInHand();
        for (ClickableItems clickableItems : ClickableItems.values()) {
            ClickableItem clickableItem = clickableItems.getClickableItem();
            if (clickableItem.getIcon().isSimilar(item)) {
                event.setCancelled(true);
                break;
            }
        }
    }
}
