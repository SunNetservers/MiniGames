package net.knarcraft.minigames.arena;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract representation of a player's entry state
 */
public abstract class AbstractPlayerEntryState implements PlayerEntryState {

    protected final Player player;
    private final boolean makePlayerInvisible;
    private final Location entryLocation;
    private final boolean originalIsFlying;
    private final GameMode originalGameMode;
    private final boolean originalAllowFlight;
    private final boolean originalInvulnerable;
    private final boolean originalIsSwimming;
    private final boolean originalCollideAble;

    /**
     * Instantiates a new abstract player entry state
     *
     * @param player              <p>The player whose state this should keep track of</p>
     * @param makePlayerInvisible <p>Whether players should be made invisible while in the arena</p>
     */
    public AbstractPlayerEntryState(@NotNull Player player, boolean makePlayerInvisible) {
        this.player = player;
        this.makePlayerInvisible = makePlayerInvisible;
        this.entryLocation = player.getLocation().clone();
        this.originalIsFlying = player.isFlying();
        this.originalGameMode = player.getGameMode();
        this.originalAllowFlight = player.getAllowFlight();
        this.originalInvulnerable = player.isInvulnerable();
        this.originalIsSwimming = player.isSwimming();
        this.originalCollideAble = player.isCollidable();
    }

    @Override
    public void setArenaState() {
        if (this.makePlayerInvisible) {
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                    PotionEffect.INFINITE_DURATION, 3));
        }
    }

    @Override
    public void restore() {
        this.player.setFlying(this.originalIsFlying);
        this.player.setGameMode(this.originalGameMode);
        this.player.setAllowFlight(this.originalAllowFlight);
        this.player.setInvulnerable(this.originalInvulnerable);
        this.player.setSwimming(this.originalIsSwimming);
        this.player.setCollidable(this.originalCollideAble);
        if (this.makePlayerInvisible) {
            this.player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

    @Override
    public Location getEntryLocation() {
        return this.entryLocation;
    }

}
