package fr.pilou.uhcapi.utils;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ComponentUtils {
    public static void sendActionBar(Player player, String message) {
        IChatBaseComponent component = new ChatComponentText(message);
        PacketPlayOutChat packet = new PacketPlayOutChat(component, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int duration, int fadeOut) {
        if (player == null) return;

        CraftPlayer craftPlayer = (CraftPlayer) player;

        PacketPlayOutTitle timing = new PacketPlayOutTitle(fadeIn, duration, fadeOut);
        craftPlayer.getHandle().playerConnection.sendPacket(timing);

        if (title != null && !title.isEmpty()) {
            IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + title + "\"}");
            PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
            craftPlayer.getHandle().playerConnection.sendPacket(titlePacket);
        }

        if (subtitle != null && !subtitle.isEmpty()) {
            IChatBaseComponent chatSubtitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + subtitle + "\"}");
            PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubtitle);
            craftPlayer.getHandle().playerConnection.sendPacket(subtitlePacket);
        }
    }

}
