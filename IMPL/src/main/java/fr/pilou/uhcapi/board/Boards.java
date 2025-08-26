package fr.pilou.uhcapi.board;

import ca.kaxx.board.KaxxScoreboardHandler;
import ca.kaxx.board.adapter.KaxxScoreboardAdapter;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.State;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Boards {
    WAITING(State.WAITING, new WaitingBoard()),
    IN_GAME(State.IN_GAME, new GameBoard()),
    ;
    private final State state;
    private final KaxxScoreboardAdapter kaxxScoreboardAdapter;

    public static void switchAdapter(){
        IGameHandler gameHandler = API.get().getGameHandler();
        State state = gameHandler.getState();
        for (Boards boards : values()) {
            if(boards.getState() == state) {
                KaxxScoreboardHandler scoreboardHandler = API.get().getScoreboardHandler();
                scoreboardHandler.setAdapter(boards.getKaxxScoreboardAdapter());
                break;
            }
        }
    }
}
