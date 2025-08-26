package fr.pilou.uhcapi.utils;

import net.minecraft.server.v1_8_R3.EnumParticle;

import javax.annotation.Nonnull;
import java.awt.Color;

public class KaxxColoredParticles extends KaxxParticles {

    /**
     * Creates a colored particle effect.
     *
     * @param particle the type of particle to display
     * @param far      whether the particles are displayed at a long distance
     * @param x        the x coordinate of the particle
     * @param y        the y coordinate of the particle
     * @param z        the z coordinate of the particle
     * @param color    the color of the particle (using {@link java.awt.Color})
     * @param count    the number of particles to display
     * @param data     additional data for the particle
     */

    public KaxxColoredParticles(final @Nonnull EnumParticle particle, final boolean far, final double x, final double y, final double z, final @Nonnull Color color, final int count, final int[] data) {
        super(particle, far, x, y, z, 0, 0, 0, 1, count, data);
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;

        if (r == 0 && g == 0 && b == 0) {
            r = 0.001f;
            g = 0.001f;
            b = 0.001f;
        }

        this.setOffsetX(r);
        this.setOffsetY(g);
        this.setOffsetZ(b);
    }
}