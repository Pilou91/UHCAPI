package fr.pilou.uhcapi;

import fr.pilou.commandapi.CommandAPI;
import fr.pilou.menuapi.MenuAPI;
import fr.pilou.potionapi.PotionAPI;
import fr.pilou.powerapi.PowerAPI;
import fr.pilou.uhcapi.commands.HostCommand;
import fr.pilou.uhcapi.utils.BiomeUtils;
import fr.pilou.uhcapi.utils.CC;
import org.bukkit.plugin.java.JavaPlugin;

public class ImplPlugin extends JavaPlugin {

    public ImplPlugin(){
        API.initializeImplementation(new Impl(this));
    }

    @Override
    public void onLoad() {
        API.get().onLoad();
    }

    @Override
    public void onEnable() {
        MenuAPI.init(this);
        PowerAPI.init(this, CC.UHC_PREFIX + " §8" + CC.SYMBOL_BAR + " §r");
        CommandAPI.init(this);
        PotionAPI.init(this);
        CommandAPI.registerMainCommands(new HostCommand());
        API.get().onEnable();
        BiomeUtils.init();
    }

    @Override
    public void onDisable() {
        API.get().onDisable();
    }
}
