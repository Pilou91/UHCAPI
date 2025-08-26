package fr.pilou.uhcapi.commands;

import com.google.common.collect.Lists;
import fr.pilou.commandapi.commands.ArgsType;
import fr.pilou.commandapi.commands.MainCommand;
import fr.pilou.commandapi.commands.SubCommand;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.State;
import fr.pilou.uhcapi.game.player.IGamePlayer;
import fr.pilou.uhcapi.menus.module.ColorMenu;
import fr.pilou.uhcapi.module.IModuleHandler;
import fr.pilou.uhcapi.module.camp.Camp;
import fr.pilou.uhcapi.module.role.Role;
import fr.pilou.uhcapi.utils.CC;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public class ModuleCommand extends MainCommand {
    private final IModuleHandler moduleHandler;

    @Override
    public String getCommandName() {
        return moduleHandler.getName();
    }

    @Override
    public List<String> getNames() {
        return List.of(moduleHandler.getCommandPrefix());
    }

    @Override
    public String getLore() {
        return "";
    }

    @Override
    public List<SubCommand> getSubCommands() {
        return List.of(new SubCommand() {
            @Override
            public String getName() {
                return "color";
            }

            @Override
            public String getLore() {
                return "";
            }

            @Override
            public boolean isMultipleArgs() {
                return true;
            }

            @Override
            public ArgsType getType() {
                return ArgsType.PLAYER;
            }

            @Override
            public void onSubCommandWithAllArgs(Player player, String[] args) {
                IGameHandler gameHandler = API.get().getGameHandler();
                if (gameHandler.getState() == State.WAITING
                        || gameHandler.getState() == State.STARTING
                        || gameHandler.getState() == State.TELEPORT) {
                    player.sendMessage(CC.modulePrefix("§cVous ne pouvez pas utiliser cette commande pour le moment."));
                    return;
                }

                List<Player> targetList = Lists.newArrayList();

                for (String arg : args) {
                    Player target = Bukkit.getPlayer(arg);
                    if (target == null) {
                        continue;
                    }

                    targetList.add(target);
                }

                if (targetList.isEmpty()) {
                    player.sendMessage(CC.modulePrefix("§cVous devez précisez un ou plusieurs pseudo."));
                    return;
                }

                new ColorMenu(targetList).open(player);
            }
        }, new SubCommand() {
            @Override
            public String getName() {
                return "roles";
            }

            @Override
            public String getLore() {
                return "";
            }

            @SneakyThrows
            @Override
            public boolean onSubCommand(Player player, Object args) {
                player.sendMessage(CC.modulePrefix("Rôles :"));
                player.sendMessage("");

                IGameHandler gameHandler = API.get().getGameHandler();
                boolean roleHasBeenDistribute = moduleHandler.isRoleHasBeenDistribute();

                for (Class<? extends Camp> campClass : moduleHandler.getCamps()) {
                    int amount = 0;
                    for (IGamePlayer gamePlayer : gameHandler.getGamePlayers()) {
                        if(roleHasBeenDistribute){
                            if (gamePlayer.getRole().getCampClass().equals(campClass) && gamePlayer.isAlive()) {
                                amount++;
                            }
                        }else{
                            for (Class<? extends Role> roleClass : moduleHandler.getComposition()) {
                                Role role = roleClass.getDeclaredConstructor().newInstance();
                                if(role.getCampClass().equals(campClass)){
                                    amount++;
                                }
                            }
                        }
                    }

                    Camp camp = campClass.getDeclaredConstructor().newInstance();
                    player.sendMessage("§7» " + camp.getColor() + "§l" + camp.getName() + " §7(" + amount + ")");
                    player.sendMessage("");

                    for (Class<? extends Role> roleClass : moduleHandler.getRoles()) {
                        Role role = roleClass.getDeclaredConstructor().newInstance();
                        if(!role.getCampClass().equals(campClass)){
                            continue;
                        }

                        if(roleHasBeenDistribute){
                            boolean alive = false;
                            for (IGamePlayer gamePlayer : gameHandler.getGamePlayers()) {
                                if(gamePlayer.getRole().getClass().equals(role.getClass()) && gamePlayer.isAlive()){
                                    alive = true;
                                    break;
                                }
                            }
                            player.sendMessage(camp.getColor() + "• " + (alive ? "§r" : "§r§m") + role.getName());
                            continue;
                        }

                        if(!moduleHandler.getComposition().contains(roleClass)){
                            continue;
                        }

                        player.sendMessage(camp.getColor() + "• §r" + role.getName());
                    }

                    player.sendMessage("");
                }
                return true;
            }
        }, new SubCommand() {
            @Override
            public String getName() {
                return "me";
            }

            @Override
            public String getLore() {
                return "";
            }

            @Override
            public boolean onSubCommand(Player player, Object args) {
                IGameHandler gameHandler = API.get().getGameHandler();
                IGamePlayer gamePlayer = gameHandler.getGamePlayer(player.getUniqueId());
                Role role = gamePlayer.getRole();
                if (role == null) {
                    return true;
                }

                role.onDescription(gamePlayer);
                return true;
            }
        });
    }
}
