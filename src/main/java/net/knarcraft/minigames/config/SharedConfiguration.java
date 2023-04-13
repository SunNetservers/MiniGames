package net.knarcraft.minigames.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * The configuration keeping track of shared settings
 */
public class SharedConfiguration extends MiniGameConfiguration {

    private final static String rootNode = "shared.";
    private double liquidHitBoxDepth;
    private double solidHitBoxDistance;

    /**
     * Instantiates a new shared configuration
     *
     * @param configuration <p>The YAML configuration to use internally</p>
     */
    public SharedConfiguration(@NotNull FileConfiguration configuration) {
        super(configuration);
    }

    /**
     * Gets the negative depth a player must reach in a liquid block for fail/win detection to trigger
     *
     * <p>This decides how far inside a non-solid block the player must go before detection triggers. The closer to -1
     * it is, the more accurate it will seem to the player, but the likelihood of not detecting the hit increases.</p>
     *
     * @return <p>The liquid hit box depth to use</p>
     */
    public double getLiquidHitBoxDepth() {
        return this.liquidHitBoxDepth;
    }

    /**
     * Gets the positive distance a player must at most be from a block for fail/win detection to trigger
     *
     * <p>This decides the distance the player must be from a block below them before a hit triggers. If too low, the
     * likelihood of detecting the hit decreases, but it won't look like the player hit the block without being near.</p>
     *
     * @return <p>The solid hit box distance to use</p>
     */
    public double getSolidHitBoxDistance() {
        return this.solidHitBoxDistance;
    }

    @Override
    protected void load() {
        this.liquidHitBoxDepth = configuration.getDouble(rootNode + "liquidHitBoxDepth", -0.8);
        this.solidHitBoxDistance = configuration.getDouble(rootNode + "solidHitBoxDistance", 0.2);

        if (this.liquidHitBoxDepth <= -1 || this.liquidHitBoxDepth > 0) {
            this.liquidHitBoxDepth = -0.8;
        }

        if (this.solidHitBoxDistance <= 0 || this.solidHitBoxDistance > 1) {
            this.solidHitBoxDistance = 0.2;
        }
    }

    @Override
    public String toString() {
        return "Current configuration:" +
                "\n" + "Liquid hit box depth: " + liquidHitBoxDepth +
                "\n" + "Solid hit box distance: " + solidHitBoxDistance;
    }

}
