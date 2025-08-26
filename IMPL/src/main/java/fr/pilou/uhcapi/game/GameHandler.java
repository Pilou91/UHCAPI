package fr.pilou.uhcapi.game;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.board.Boards;
import fr.pilou.uhcapi.game.inventory.IInventoryHandler;
import fr.pilou.uhcapi.game.player.GamePlayer;
import fr.pilou.uhcapi.game.player.IGamePlayer;
import fr.pilou.uhcapi.game.scenarios.IScenariosHandler;
import fr.pilou.uhcapi.tasks.GameRunnable;
import fr.pilou.uhcapi.listeners.ListenerRegistry;
import fr.pilou.uhcapi.utils.ComponentUtils;
import fr.pilou.uhcapi.utils.ReflectionUtils;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

@Data
public class GameHandler implements IGameHandler {
    private final Map<UUID, IGamePlayer> gamePlayersMap;
    private final Map<Class<?>, Listener> registeredListeners = Maps.newHashMap();

    private String host = "";
    private List<String> coHost;
    private State state;
    private boolean pvp;
    private int group;

    private int currentTime;

    public GameHandler() {
        this.gamePlayersMap = Maps.newHashMap();
        this.coHost = Lists.newArrayList();
    }

    @Override
    public List<IGamePlayer> getGamePlayers() {
        return new ArrayList<>(getGamePlayersMap().values());
    }

    @Override
    public Map<UUID, IGamePlayer> getGamePlayersMap() {
        return gamePlayersMap;
    }

    @Override
    public IGamePlayer getGamePlayer(UUID uuid) {
        return gamePlayersMap.computeIfAbsent(uuid, GamePlayer::new);
    }

    @Override
    public void addCoHost(String... names) {
        coHost.addAll(Arrays.asList(names));
    }

    public void setState(State newState) {
        this.state = newState;
        Boards.switchAdapter();

        List<Class<?>> listenerClasses = ReflectionUtils.getClassWithAnnotation(API.get().getPlugin(), ListenerRegistry.class);

        for (Class<?> clazz : listenerClasses) {
            if (!Listener.class.isAssignableFrom(clazz)) continue;

            ListenerRegistry registry = clazz.getAnnotation(ListenerRegistry.class);
            List<State> applicableStates = Arrays.asList(registry.state());

            boolean shouldBeActive = applicableStates.isEmpty() || applicableStates.contains(newState);
            boolean isAlreadyRegistered = registeredListeners.containsKey(clazz);

            if (!shouldBeActive && isAlreadyRegistered) {
                Listener listener = registeredListeners.remove(clazz);
                HandlerList.unregisterAll(listener);
                System.out.println("(UHC API) UNREGISTER CLASS " + clazz.getSimpleName());
                continue;
            }

            if (shouldBeActive && !isAlreadyRegistered) {
                try {
                    Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                    Bukkit.getPluginManager().registerEvents(listener, API.get().getPlugin());
                    registeredListeners.put(clazz, listener);
                    System.out.println("(UHC API) REGISTER CLASS " + clazz.getSimpleName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void inGame() {
        setState(State.IN_GAME);

        new GameRunnable(this).runTaskTimer(API.get().getPlugin(), 0L, 1L);

        IScenariosHandler scenariosHandler = API.get().getScenariosHandler();
        scenariosHandler.handleScenarios();

        for (IGamePlayer gamePlayer : getGamePlayers()) {
            Player player = gamePlayer.getPlayer();
            if (player == null) {
                continue;
            }

            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1f, 1f);
            ComponentUtils.sendTitle(player, "§f» §ePartie Lancée §7(§a✔§7) §f«", "Bonne chance à tous !", 20, 20, 20);

            PlayerInventory inventory = player.getInventory();
            inventory.clear();
            inventory.setHelmet(null);
            inventory.setChestplate(null);
            inventory.setLeggings(null);
            inventory.setBoots(null);
            player.updateInventory();

            player.setGameMode(GameMode.SURVIVAL);

            IInventoryHandler inventoryHandler = API.get().getInventoryHandler();
            inventoryHandler.applyInventory(player.getInventory());
        }
    }
}
