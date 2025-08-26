package fr.pilou.uhcapi.game.timer.impl;

import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.timers.Timer;
import fr.pilou.uhcapi.game.timers.TimerRegistry;
import fr.pilou.uhcapi.utils.CC;
import fr.pilou.uhcapi.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@TimerRegistry(name = "PvP", activationTime = 30)
public class PvpTimer extends Timer {
    @Override
    public ItemStack getIcon() {
        return new ItemBuilder(Material.DIAMOND_SWORD)
                .build();
    }

    @Override
    public void run(int currentTime) {
        int activationTime = getActivationTime();
        int secondsRemaining = activationTime - currentTime;

        if (secondsRemaining > 0 && secondsRemaining <= 5) {
            String secondLabel = secondsRemaining == 1 ? "seconde" : "secondes";
            Bukkit.broadcastMessage(CC.uhcPrefix("Activation du §ePvP §rdans " + secondsRemaining + " " + secondLabel + "."));
        }

        if (currentTime == activationTime) {
            Bukkit.broadcastMessage(CC.uhcPrefix("Activation du §ePvP§r."));
            API.get().getGameHandler().setPvp(true);
        }
    }
}
