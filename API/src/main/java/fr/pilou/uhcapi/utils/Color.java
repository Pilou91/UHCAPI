package fr.pilou.uhcapi.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public enum Color {
    WHITE("Blanc", ChatColor.WHITE, 15),
    RED("Rouge", ChatColor.RED, 1),
    BLUE("Bleu", ChatColor.BLUE, 4),
    GREEN("Vert", ChatColor.GREEN, 10),
    DARK_GREEN("Vert Foncé", ChatColor.DARK_GREEN, 2),
    PINK("Rose", ChatColor.LIGHT_PURPLE, 9),
    PURPLE("Violet", ChatColor.DARK_PURPLE, 5),
    YELLOW("Jaune", ChatColor.YELLOW, 11),
    DARK_GRAY("Gris Foncé", ChatColor.DARK_GRAY, 8),
    ORANGE("Orange", ChatColor.GOLD, 14),
    LIGHT_BLUE("Bleu Clair", ChatColor.AQUA, 12),
    CYAN("Cyan", ChatColor.DARK_AQUA, 6),
    GRAY("Gris", ChatColor.GRAY, 7),
    BLACK("Noir", ChatColor.BLACK, 0),
    ;

    private final String name;
    private final ChatColor color;
    private final int data;

    public static Color getFromChatColor(ChatColor color){
        for (Color value : values()) {
            if (value.getColor().equals(color)) {
                return value;
            }
        }
        return null;
    }

    public static ItemStack getIconFromChatColor(ChatColor color){
        for (Color value : values()) {
            if (value.getColor().equals(color)) {
                return value.getIcon();
            }
        }
        return null;
    }

    public ItemStack getIcon() {
        return new ItemBuilder(Material.INK_SACK)
                .setDurability(getData())
                .build();
    }
}