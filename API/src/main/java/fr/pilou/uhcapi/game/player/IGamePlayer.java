package fr.pilou.uhcapi.game.player;

import fr.pilou.potionapi.potions.CustomPotionType;
import fr.pilou.uhcapi.module.role.Role;
import fr.pilou.uhcapi.utils.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;

public interface IGamePlayer {
    UUID getUniqueId();

    Player getPlayer();

    String getName();

    int getKills();

    void setKills(int kills);

    int getAssists();

    void setAssists(int assists);

    boolean isAlive();

    void setAlive(boolean alive);

    int getDisconnectTime();

    void setDisconnectTime(int disconnectTime);

    Map<UUID, String> getColors();

    void setColor(Player target, Color color);

    Role getRole();

    void setRole(Role role);

    void sendMessage(String message);

    boolean isInventoryEdit();

    void setInventoryEdit(boolean inventoryEdit);

    void loadHotBar();

    void addPotion(CustomPotionType type, int level);

    void addPotion(CustomPotionType type, int level, int duration);

    void removePotion(CustomPotionType type);

    void clearPotionsEffects();

    void sendPlayerNameOfRole(Class<? extends Role> roleClass);

    PlayerInventory getDeathInventory();

    void die(Player killer, PlayerInventory inventory);

    void revive();
}
