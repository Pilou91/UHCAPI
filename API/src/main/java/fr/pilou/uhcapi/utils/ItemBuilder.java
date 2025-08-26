package fr.pilou.uhcapi.utils;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemStack build() {
        return itemStack;
    }

    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);
        return this;
    }


    public ItemBuilder setGlow(boolean glowing) {
        if (glowing) {
            itemStack.addUnsafeEnchantment(Enchantment.LURE, 1);
            addItemFlag(ItemFlag.HIDE_ENCHANTS);
            return this;
        }

        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level){
        itemMeta.addEnchant(enchantment, level, true);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setDurability(int durability) {
        itemStack.setDurability((short) durability);
        return this;
    }

    public ItemBuilder setName(String name) {
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if(lore.isEmpty()){
            return this;
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(String[] lore) {
        if(lore.length == 0){
            return this;
        }

        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);
        return this;
    }
}
