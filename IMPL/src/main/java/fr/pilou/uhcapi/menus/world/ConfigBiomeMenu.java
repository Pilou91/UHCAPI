package fr.pilou.uhcapi.menus.world;

import com.google.common.collect.Lists;
import fr.pilou.menuapi.buttons.Button;
import fr.pilou.menuapi.buttons.impl.BackButton;
import fr.pilou.menuapi.menu.impl.PaginatedMenu;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.world.IWorldHandler;
import fr.pilou.uhcapi.utils.BiomeUtils;
import fr.pilou.uhcapi.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ConfigBiomeMenu extends PaginatedMenu {
    private IWorldHandler worldHandler = API.get().getWorldHandler();
    private final boolean central;

    @Override
    public String getTitle(Player player) {
        return "§f» §e§lConfiguration du Biome " + (central ? "Central" : "Secondaire");
    }

    @Override
    public int getGlassColor() {
        return 4;
    }

    @Override
    public List<Button> getPaginatedButtons(Player player) {
        List<Button> list = Lists.newArrayList();
        for (Biome biome : Biome.values()) {
            BiomeUtils.BiomeInfo biomeInfo = BiomeUtils.getBiomeInfo(biome);
            if(biomeInfo == null){
                continue;
            }
            list.add(new Button() {
                @Override
                public ItemStack getIcon(Player player) {
                    return new ItemBuilder(biomeInfo.getItemStack())
                            .setName("§f» §e" + biomeInfo.getName())
                            .build();
                }

                @Override
                public void click(Player player, ClickType clickType) {
                    if (central) {
                        worldHandler.setCenterBiome(biome);
                    } else {
                        worldHandler.setSecondaryBiome(biome);
                    }
                    new WorldMenu().open(player);
                }
            });
        }
        return list;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Map.of(49, new BackButton(new WorldMenu()));
    }
}
