package fr.pilou.uhcapi.board;

import ca.kaxx.board.KaxxScoreboardHandler;
import ca.kaxx.board.adapter.KaxxScoreboardAdapter;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.utils.CC;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class GameBoard implements KaxxScoreboardAdapter {
    @Override
    public String getTitle(@Nonnull Player player) {
        return "§f» §e§lUHC §f«";
    }

    @Override
    public Collection<String> getLines(@Nonnull Player player) {
        IGameHandler gameHandler = API.get().getGameHandler();
        KaxxScoreboardHandler scoreboardHandler = API.get().getScoreboardHandler();
        return List.of(
                "§1",
                " §fDurée: §e" + CC.time(gameHandler.getCurrentTime()),
                "§2",
                scoreboardHandler.getScoreboardAnimation().getAnimatedText()
        );
    }
}
