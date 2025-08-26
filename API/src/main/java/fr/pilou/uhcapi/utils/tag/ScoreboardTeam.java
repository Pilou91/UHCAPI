package fr.pilou.uhcapi.utils.tag;


import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Alexis on 19/02/2017.
 */
@Setter
@Getter
public class ScoreboardTeam {

    private String suffix;
    private String name;
    private String prefix;

    /**
     * Initialise un nouveau ScoreboardTeam
     * @param name Le nom de la team
     * @param prefix Le préfix de la team
     */
    public ScoreboardTeam(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = "";
    }

    /**
     * Initialise un nouveau ScoreboardTeam
     * @param name Le nom de la team
     */
    public ScoreboardTeam(String name) {
        this.name = name;
        this.prefix = "";
        this.suffix = "";
    }

    /**
     * Initialise un nouveau ScoreboardTeam
     * @param name Le nom de la team
     * @param prefix Le préfix de la team
     * @param suffix Le suffix de la team
     */
    public ScoreboardTeam(String name, String prefix, String suffix) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * Renvoie le packet permettant de changer le pseudo d'un joueur
     * @param player Le joueur auquel on change le pseudo
     * @return Le packet permettant de changer le pseudo d'un joueur
     */
    public static PacketPlayOutPlayerInfo updateDisplayName(EntityPlayer player) {
        PacketPlayOutPlayerInfo packet =
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, player);
        player.playerConnection.sendPacket(packet);
        return packet;
    }

    /**
     * <p>
     * 0 si une team est créée
     * <p>
     * 1 si une team est supprimée
     * <p>
     * 2 si les informations d'une team sont mises à jour
     * <p>
     * 3 si un nouveau joueur rejoint l'équipe
     * <p>
     * 4 si un ancien joueur quitte l'équipe
     */
    private PacketPlayOutScoreboardTeam createPacket(int mode) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

        setField(packet, "a", name);
        setField(packet, "h", mode);
        setField(packet, "b", "");
        setField(packet, "c", prefix);
        setField(packet, "d", suffix);
        setField(packet, "i", 0);
        setField(packet, "e", "always");
        setField(packet, "f", 0);

        return packet;
    }

    /**
     * Renvoie le packet servant à créer une team
     * @return Le packet servant à créer une team
     */
    public PacketPlayOutScoreboardTeam createTeam() {
        return createPacket(0);
    }

    /**
     * Renvoie le packet servant à modifier une team
     * @return Le packet servant à modifier une team
     */
    public PacketPlayOutScoreboardTeam updateTeam() {
        return createPacket(2);
    }

    /**
     * Renvoie le packet servant à supprimer une team
     * @return Le packet servant à supprimer une team
     */
    public PacketPlayOutScoreboardTeam removeTeam() {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        setField(packet, "a", name);
        setField(packet, "h", 1);

        return packet;
    }

    /**
     * Renvoie le packet servant à mettre ou démettre le frendly fire
     * @param active Défini si le frendly fire doit être activé ou désactivé
     * @return Le packet servant à mettre ou démettre le frendly fire
     */
    public PacketPlayOutScoreboardTeam setFriendlyFire(boolean active) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        setField(packet, "i", (active ? 1 : 0));

        return packet;
    }

    /**
     * Renvoie le packet servant à ajouter ou retirer le joueur
     * @param mode 3 : Ajouter, 4 : Retirer
     * @param playerName Le nom du joueur
     * @return Le packet servant à ajouter ou retirer le joueur
     */
    @SuppressWarnings("unchecked")
    public PacketPlayOutScoreboardTeam addOrRemovePlayer(int mode, String playerName) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        setField(packet, "a", name);
        setField(packet, "h", mode);

        try {
            Field f = packet.getClass().getDeclaredField("g");
            f.setAccessible(true);
            ((List<String>) f.get(packet)).add(playerName);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return packet;
    }

    /**
     * <p>
     * <p> a : nom de l'équipe
     * <p>
     * <p> b : nom affiché
     * <p>
     * <p> c : prefix
     * <p>
     * <p> d : suffix
     * <p>
     * <p> e : la visibilité du nom (always, never)
     * <p>
     * <p> f : la couleur de l'équipe
     * <p>
     * <p> h : mode
     * <p>
     * <p> i : friendly fire (0 off, 1 on)
     */
    private void setField(Object edit, String fieldName, Object value) {
        try {
            Field field = edit.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(edit, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
