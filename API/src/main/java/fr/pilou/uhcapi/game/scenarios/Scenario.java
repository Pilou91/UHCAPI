package fr.pilou.uhcapi.game.scenarios;

import fr.pilou.uhcapi.game.IGameHandler;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Material;

@Getter
public abstract class Scenario {
    private final String name;
    private final String[] lore;
    private final Material material;
    private final int data;

    @SneakyThrows
    public Scenario() {
        ScenarioRegistry registry = getClass().getDeclaredAnnotation(ScenarioRegistry.class);
        if (registry == null) {
            throw new Exception("Scenario data must not be null");
        }

        this.name = registry.name();
        this.material = registry.material();
        this.data = registry.data();
        this.lore = registry.lore();
    }

    public void handle(IGameHandler gameHandler){

    }
}
