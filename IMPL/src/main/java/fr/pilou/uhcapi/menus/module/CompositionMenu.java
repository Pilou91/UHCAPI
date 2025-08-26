package fr.pilou.uhcapi.menus.module;

import com.google.common.collect.Lists;
import fr.pilou.menuapi.buttons.Button;
import fr.pilou.menuapi.buttons.impl.BackButton;
import fr.pilou.menuapi.menu.impl.PaginatedMenu;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.menus.MainMenu;
import fr.pilou.uhcapi.module.IModuleHandler;
import fr.pilou.uhcapi.module.camp.Camp;
import fr.pilou.uhcapi.module.role.Role;
import fr.pilou.uhcapi.utils.Color;
import fr.pilou.uhcapi.utils.ItemBuilder;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class CompositionMenu extends PaginatedMenu {
    @Override
    public String getTitle(Player player) {
        return "§f» §e§lComposition";
    }

    @Override
    public int getGlassColor() {
        return 4;
    }

    @SneakyThrows
    @Override
    public List<Button> getPaginatedButtons(Player player) {
        List<Button> buttons = Lists.newArrayList();
        IModuleHandler moduleHandler = API.get().getModuleHandler();
        for (Class<? extends Camp> campClass : moduleHandler.getCamps()) {
            Camp camp = campClass.getDeclaredConstructor().newInstance();
            buttons.add(new Button() {
                @Override
                public ItemStack getIcon(Player player) {
                    return new ItemBuilder(Color.getIconFromChatColor(camp.getColor()))
                            .setName("§f» " + camp.getColor() + "§l" + camp.getName())
                            .build();
                }

                @Override
                public void click(Player player, ClickType clickType) {
                    new PaginatedMenu(){
                        @Override
                        public String getTitle(Player player) {
                            return "§f» §e§lComposition";
                        }

                        @Override
                        public int getGlassColor() {
                            return 4;
                        }

                        @SneakyThrows
                        @Override
                        public List<Button> getPaginatedButtons(Player player) {
                            List<Button> buttons = Lists.newArrayList();
                            IModuleHandler moduleHandler = API.get().getModuleHandler();
                            for (Class<? extends Role> roleClass : moduleHandler.getRoles()) {
                                List<Class<? extends Role>> composition = moduleHandler.getComposition();
                                boolean inComposition = composition.contains(roleClass);
                                Role role = roleClass.getDeclaredConstructor().newInstance();
                                if(!role.getCampClass().equals(campClass)){
                                    continue;
                                }

                                buttons.add(new Button() {
                                    @Override
                                    public ItemStack getIcon(Player player) {
                                        return new ItemBuilder(Material.STAINED_CLAY)
                                                .setDurability(inComposition ? 5 : 14)
                                                .setName("§f» " + camp.getColor() + "§l" + role.getName())
                                                .build();
                                    }

                                    @Override
                                    public void click(Player player, ClickType clickType) {
                                        if(inComposition){
                                            composition.remove(roleClass);
                                            return;
                                        }

                                        composition.add(roleClass);
                                    }

                                    @Override
                                    public boolean update(Player player, ClickType clickType) {
                                        return true;
                                    }
                                });
                            }
                            return buttons;
                        }

                        @Override
                        public Map<Integer, Button> getButtons(Player player) {
                            return Map.of(49, new BackButton(new CompositionMenu()));
                        }
                    }.open(player);
                }
            });
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Map.of(49, new BackButton(new MainMenu()));
    }
}
