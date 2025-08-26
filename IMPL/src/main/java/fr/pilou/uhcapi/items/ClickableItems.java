package fr.pilou.uhcapi.items;

import fr.pilou.uhcapi.items.impl.MainClickableItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClickableItems {
    MAIN(new MainClickableItem()),
    ;
    private final ClickableItem clickableItem;
}
