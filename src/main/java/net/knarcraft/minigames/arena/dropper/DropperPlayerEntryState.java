package net.knarcraft.minigames.arena.dropper;

import net.knarcraft.minigames.arena.AbstractPlayerEntryState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The state of a player before entering a dropper arena
 */
public class DropperPlayerEntryState extends AbstractPlayerEntryState {

    private final float originalFlySpeed;
    private final boolean disableHitCollision;
    private final float horizontalVelocity;
    private final DropperArenaGameMode arenaGameMode;

    /**
     * Instantiates a new player state
     *
     * @param player <p>The player whose state should be stored</p>
     */
    public DropperPlayerEntryState(@NotNull Player player, @NotNull DropperArenaGameMode arenaGameMode,
                                   boolean makePlayerInvisible, boolean disableHitCollision, float horizontalVelocity) {
        super(player, makePlayerInvisible);
        this.originalFlySpeed = player.getFlySpeed();
        this.arenaGameMode = arenaGameMode;
        this.disableHitCollision = disableHitCollision;
        this.horizontalVelocity = horizontalVelocity;
    }

    @Override
    public void setArenaState() {
        super.setArenaState();
        this.player.setAllowFlight(true);
        this.player.setFlying(true);
        this.player.setGameMode(GameMode.ADVENTURE);
        this.player.setSwimming(false);
        if (this.disableHitCollision) {
            this.player.setCollidable(false);
        }

        // If playing on the inverted game-mode, negate the horizontal velocity to swap the controls
        if (this.arenaGameMode == DropperArenaGameMode.INVERTED) {
            this.player.setFlySpeed(-this.horizontalVelocity);
        } else {
            this.player.setFlySpeed(this.horizontalVelocity);
        }
    }

    @Override
    public void restore() {
        super.restore();
        this.player.setFlySpeed(this.originalFlySpeed);
    }

}
