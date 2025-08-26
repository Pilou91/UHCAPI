package fr.pilou.uhcapi.menus.scenarios;

import com.google.common.collect.Lists;
import fr.pilou.menuapi.buttons.Button;
import fr.pilou.menuapi.buttons.impl.BackButton;
import fr.pilou.menuapi.menu.impl.PaginatedMenu;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.scenarios.IScenariosHandler;
import fr.pilou.uhcapi.game.scenarios.Scenario;
import fr.pilou.uhcapi.menus.MainMenu;
import fr.pilou.uhcapi.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class ScenariosMenu extends PaginatedMenu {
    @Override
    public String getTitle(Player player) {
        return "§f» §e§lScenarios";
    }

    @Override
    public int getGlassColor() {
        return 4;
    }

    @Override
    public List<Button> getPaginatedButtons(Player player) {
        List<Button> list = Lists.newArrayList();
        IScenariosHandler scenariosHandler = API.get().getScenariosHandler();
        for (Scenario scenario : scenariosHandler.getScenarios()) {
            list.add(new Button() {
                private boolean enabled = scenariosHandler.isEnabled(scenario);

                @Override
                public ItemStack getIcon(Player player) {
                    return new ItemBuilder(scenario.getMaterial())
                            .setDurability(scenario.getData())
                            .setName("§f» §e" + scenario.getName() + (enabled ? " §r(§aActivé§r)" : " §r(§cDésactivé§r)"))
                            .setLore(scenario.getLore())
                            .setGlow(enabled)
                            .build();
                }

                @Override
                public void click(Player player, ClickType clickType) {
                    if (enabled) {
                        scenariosHandler.disableScenario(scenario);
                        return;
                    }

                    scenariosHandler.enableScenario(scenario);
                }

                @Override
                public boolean update(Player player, ClickType clickType) {
                    return true;
                }
            });
        }
        return list;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Map.of(49, new BackButton(new MainMenu()));
    }
}
