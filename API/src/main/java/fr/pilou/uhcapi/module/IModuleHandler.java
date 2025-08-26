package fr.pilou.uhcapi.module;

import fr.pilou.uhcapi.game.player.IGamePlayer;
import fr.pilou.uhcapi.module.camp.Camp;
import fr.pilou.uhcapi.module.role.Role;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface IModuleHandler {
    JavaPlugin getPlugin();

    void setPlugin(JavaPlugin plugin);

    String getName();

    void setName(String name);

    String getCommandPrefix();

    void setCommandPrefix(String commandPrefix);

    String getChatPrefix();

    void setChatPrefix(String chatPrefix);

    List<Class<? extends Role>> getRoles();

    List<Class<? extends Role>> getComposition();

    List<Class<? extends Camp>> getCamps();

    boolean isRoleHasBeenDistribute();

    void setRoleHasBeenDistribute(boolean value);

    IGamePlayer getPlayerNameOfRole(Role role);
}
