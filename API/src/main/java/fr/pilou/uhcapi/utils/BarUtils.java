package fr.pilou.uhcapi.utils;

import com.google.common.base.Strings;
import org.bukkit.ChatColor;

public class BarUtils {
    public static String getBar(int current, int max, int barLength) {
        int percentage = (int) ((current * 100.0) / max);
        ChatColor color = getColor(percentage);

        int filledBars = (int) ((percentage / 100.0) * barLength);
        int emptyBars = barLength - filledBars;

        return Strings.repeat(color + "|", filledBars) +
                Strings.repeat("§7|", emptyBars) +
                " " + color + percentage + "%";
    }

    public static String getBar(double percentage, int barLength) {
        ChatColor color = getColor((int) percentage);

        int filledBars = (int) ((percentage / 100.0) * barLength);
        int emptyBars = barLength - filledBars;

        return Strings.repeat(color + "|", filledBars) +
                Strings.repeat("§7|", emptyBars) +
                " " + color + (int) percentage + "%";
    }

    public static ChatColor getColor(int percentage){
        if (percentage <= 30) {
            return ChatColor.RED;
        } else if (percentage <= 50) {
            return ChatColor.GOLD;
        } else if (percentage <= 70) {
            return ChatColor.YELLOW;
        } else if (percentage <= 100) {
            return ChatColor.GREEN;
        }
        return ChatColor.GRAY;
    }
}
