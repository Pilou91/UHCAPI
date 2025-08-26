package fr.pilou.uhcapi.module;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.pilou.commandapi.CommandAPI;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.commands.ModuleCommand;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.player.IGamePlayer;
import fr.pilou.uhcapi.module.camp.Camp;
import fr.pilou.uhcapi.module.camp.CampRegistry;
import fr.pilou.uhcapi.module.role.Role;
import fr.pilou.uhcapi.module.role.RoleRegistry;
import fr.pilou.uhcapi.utils.ReflectionUtils;
import lombok.Data;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Data
public class ModuleHandler implements IModuleHandler {
    private boolean roleHasBeenDistribute;
    private final List<Class<? extends Role>> roles = Lists.newArrayList();
    private final List<Class<? extends Role>> composition = Lists.newArrayList();
    private final List<Class<? extends Camp>> camps  = Lists.newArrayList();
    private JavaPlugin plugin;
    private String name;
    private String commandPrefix;
    private String chatPrefix;

    @SneakyThrows
    @Override
    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
        System.out.println("(MODULE) Plugin -> " + plugin);
        API.get().getScenariosHandler().registerScenarios(plugin);
        API.get().getTimerHandler().registerTimers(plugin);
        List<Class<?>> rolesClasses = ReflectionUtils.getClassWithAnnotation(plugin, RoleRegistry.class);
        for (Class<?> clazz : rolesClasses) {
            roles.add((Class<? extends Role>) clazz);
            System.out.println("(MODULE) Role -> " + clazz.getSimpleName());
        }

        Map<Integer, Class<? extends Camp>> campsMap = Maps.newHashMap();
        List<Class<?>> campsClasses = ReflectionUtils.getClassWithAnnotation(plugin, CampRegistry.class);

        for (Class<?> clazz : campsClasses) {
            if (!Camp.class.isAssignableFrom(clazz)) {
                System.err.println("Skipped class " + clazz.getName() + " - not a Camp");
                continue;
            }

            @SuppressWarnings("unchecked")
            Class<? extends Camp> campClass = (Class<? extends Camp>) clazz;

            try {
                Camp camp = campClass.getDeclaredConstructor().newInstance();
                int rating = camp.getRating();

                if (campsMap.containsKey(rating)) {
                    System.err.println("Duplicate rating " + rating + " for class " + campClass.getSimpleName());
                }

                campsMap.put(rating, campClass);
                System.out.println("(MODULE) Camp -> " + clazz.getSimpleName());
            } catch (Exception e) {
                System.err.println("Failed to instantiate " + campClass.getName());
                e.printStackTrace();
            }
        }

        for (Map.Entry<Integer, Class<? extends Camp>> entry : campsMap.entrySet()) {
            while (camps.size() <= entry.getKey()) {
                camps.add(null);
            }
            camps.set(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void setCommandPrefix(String commandPrefix) {
        this.commandPrefix = commandPrefix;
        CommandAPI.registerMainCommands(new ModuleCommand(this));
    }

    @Override
    public IGamePlayer getPlayerNameOfRole(Role role) {
        IGameHandler gameHandler = API.get().getGameHandler();
        for (IGamePlayer gamePlayer : gameHandler.getGamePlayers()) {
            Role checkingRole = gamePlayer.getRole();
            if(checkingRole == null){
                continue;
            }

            if(checkingRole.getClass().equals(role.getClass())){
                return gamePlayer;
            }
        }
        return null;
    }

    @Override
    public List<Class<? extends Role>> getRoles() {
        return roles;
    }
}
