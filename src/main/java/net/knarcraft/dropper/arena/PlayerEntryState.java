package net.knarcraft.dropper.arena;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

/**
 * The state of a player before entering a dropper arena
 */
public class PlayerEntryState {

    private final Player player;
    private final Location entryLocation;
    private final boolean originalIsFlying;
    private final float originalFlySpeed;
    private final GameMode originalGameMode;
    private final boolean originalAllowFlight;
    private final boolean originalInvulnerable;
    private final boolean originalIsSwimming;
    private final boolean originalCollideAble;
    private final boolean makePlayerInvisible;
    private final boolean disableHitCollision;
    private final ArenaGameMode arenaGameMode;

    /**
     * Instantiates a new player state
     *
     * @param player <p>The player whose state should be stored</p>
     */
    public PlayerEntryState(@NotNull Player player, @NotNull ArenaGameMode arenaGameMode, boolean makePlayerInvisible,
                            boolean disableHitCollision) {
        this.player = player;
        this.entryLocation = player.getLocation().clone();
        this.originalFlySpeed = player.getFlySpeed();
        this.originalIsFlying = player.isFlying();
        this.originalGameMode = player.getGameMode();
        this.originalAllowFlight = player.getAllowFlight();
        this.originalInvulnerable = player.isInvulnerable();
        this.originalIsSwimming = player.isSwimming();
        this.arenaGameMode = arenaGameMode;
        this.originalCollideAble = player.isCollidable();
        this.makePlayerInvisible = makePlayerInvisible;
        this.disableHitCollision = disableHitCollision;
    }

    /**
     * Sets the state of the stored player to the state used by arenas
     *
     * @param horizontalVelocity <p>The horizontal velocity to apply to the player</p>
     */
    public void setArenaState(float horizontalVelocity) {
        this.player.setAllowFlight(true);
        this.player.setFlying(true);
        this.player.setGameMode(GameMode.ADVENTURE);
        this.player.setSwimming(false);
        if (this.disableHitCollision) {
            this.player.setCollidable(false);
        }
        if (this.makePlayerInvisible) {
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                    PotionEffect.INFINITE_DURATION, 3));
        }

        // If playing on the inverted game-mode, negate the horizontal velocity to swap the controls
        if (arenaGameMode == ArenaGameMode.INVERTED) {
            this.player.setFlySpeed(-horizontalVelocity);
        } else {
            this.player.setFlySpeed(horizontalVelocity);
        }
    }

    /**
     * Restores the stored state for the stored player
     */
    public void restore() {
        this.player.setFlying(this.originalIsFlying);
        this.player.setGameMode(this.originalGameMode);
        this.player.setAllowFlight(this.originalAllowFlight);
        this.player.setFlySpeed(this.originalFlySpeed);
        this.player.setInvulnerable(this.originalInvulnerable);
        this.player.setSwimming(this.originalIsSwimming);
        if (this.disableHitCollision) {
            this.player.setCollidable(this.originalCollideAble);
        }
        if (this.makePlayerInvisible) {
            this.player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

    /**
     * Gets the location the player entered from
     *
     * @return <p>The location the player entered from</p>
     */
    public Location getEntryLocation() {
        return this.entryLocation;
    }

}
