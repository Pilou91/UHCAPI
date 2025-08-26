package fr.pilou.uhcapi.module.camp;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;

@Getter
public abstract class Camp {
    private final String name;
    private final ChatColor color;
    private final boolean alone;
    private final int rating;

    @SneakyThrows
    public Camp(){
        CampRegistry registry = getClass().getDeclaredAnnotation(CampRegistry.class);
        if (registry == null) {
            throw new Exception("Scenario data must not be null");
        }

        this.name = registry.name();
        this.color = registry.color();
        this.alone = registry.alone();
        this.rating = registry.rating();
    }
}
