package fr.pilou.uhcapi.game;

import fr.pilou.uhcapi.game.player.IGamePlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IGameHandler {
    String getHost();

    void setHost(String name);

    List<String> getCoHost();

    void addCoHost(String... names);

    List<IGamePlayer> getGamePlayers();

    Map<UUID, IGamePlayer> getGamePlayersMap();

    IGamePlayer getGamePlayer(UUID uuid);

    State getState();

    void setState(State state);

    int getCurrentTime();

    void setCurrentTime(int currentTime);

    boolean isPvp();

    void setPvp(boolean pvp);

    int getGroup();

    void setGroup(int group);

    void inGame();
}
