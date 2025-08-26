package fr.pilou.uhcapi.utils;

import lombok.Data;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;


@Data
public class KaxxParticles {

    private EnumParticle particle;
    private boolean far;
    private double x;
    private double y;
    private double z;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private float speed;
    private int count;
    private int[] data;

    public KaxxParticles(final @Nonnull EnumParticle particle, final double x, final double y, final double z) {
        this(particle, false, x, y, z, 0, 0, 0, 0, 0, new int[0]);
    }

    public KaxxParticles(final @Nonnull EnumParticle particle, final boolean far, final double x, final double y, final double z) {
        this(particle, far, x, y, z, 0, 0, 0, 0, 0, new int[0]);
    }

    public KaxxParticles(final @Nonnull EnumParticle particle, final boolean far, final double x, final double y, final double z, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int count, final int[] data) {
        this.particle = particle;
        this.far = far;
        this.x = x;
        this.y = y;
        this.z = z;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
        this.count = count;
        this.data = data;
    }

    public void send(final @Nonnull Player player) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, far, (float) x, (float) y, (float) z, offsetX, offsetY, offsetZ, speed, count, data);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}