package fr.pilou.uhcapi.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum State {
    WAITING(),
    STARTING(),
    TELEPORT(),
    IN_GAME(),
    FINISH(),
    ;
}
