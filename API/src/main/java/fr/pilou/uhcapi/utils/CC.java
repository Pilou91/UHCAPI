package fr.pilou.uhcapi.utils;

import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.module.IModuleHandler;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CC {
    public final String SYMBOL_BAR = "❙";
    public final String UHC_PREFIX = "§e§lUHC";

    public String uhcPrefix(String string){
        return UHC_PREFIX + " §8" + SYMBOL_BAR + " §r" + string;
    }

    public String modulePrefix(String string){
        IModuleHandler moduleHandler = API.get().getModuleHandler();
        String chatPrefix = moduleHandler.getChatPrefix();
        return chatPrefix + " §8" + SYMBOL_BAR + " §r" + string;
    }

    public String time(int time) {
        int hours = time / 3600;
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;

        StringBuilder result = new StringBuilder();

        if (hours > 0) {
            result.append(String.format("%02d:", hours));
        }

        if (minutes > 0 || hours > 0) {
            result.append(String.format("%02d:", minutes));
        }

        result.append(String.format("%02d", seconds));

        return result.toString();
    }
    public String timeWithLetter(int time) {
        int hours = time / 3600;
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;

        StringBuilder result = new StringBuilder();

        if (hours > 0) {
            result.append(String.format("%02dh", hours));
        }

        if (minutes > 0 || hours > 0) {
            result.append(String.format("%02dm", minutes));
        }

        result.append(String.format("%02ds", seconds));

        return result.toString();
    }
}
