package net.knarcraft.minigames.arena.dropper;

import net.knarcraft.minigames.arena.AbstractPlayerEntryState;
import net.knarcraft.minigames.container.SerializableUUID;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

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

    /**
     * Instantiates a new parkour player entry state
     *
     * @param playerId             <p>The id of the player whose state this should keep track of</p>
     * @param makePlayerInvisible  <p>Whether players should be made invisible while in the arena</p>
     * @param entryLocation        <p>The location the player entered from</p>
     * @param originalIsFlying     <p>Whether the player was flying before entering the arena</p>
     * @param originalGameMode     <p>The game-mode of the player before entering the arena</p>
     * @param originalAllowFlight  <p>Whether the player was allowed flight before entering the arena</p>
     * @param originalInvulnerable <p>Whether the player was invulnerable before entering the arena</p>
     * @param originalIsSwimming   <p>Whether the player was swimming before entering the arena</p>
     * @param originalCollideAble  <p>Whether the player was collide-able before entering the arena</p>
     */
    public DropperPlayerEntryState(@NotNull UUID playerId, boolean makePlayerInvisible, Location entryLocation,
                                   boolean originalIsFlying, GameMode originalGameMode, boolean originalAllowFlight,
                                   boolean originalInvulnerable, boolean originalIsSwimming,
                                   boolean originalCollideAble, float originalFlySpeed, boolean disableHitCollision,
                                   float horizontalVelocity, DropperArenaGameMode arenaGameMode) {
        super(playerId, makePlayerInvisible, entryLocation, originalIsFlying, originalGameMode, originalAllowFlight,
                originalInvulnerable, originalIsSwimming, originalCollideAble);
        this.originalFlySpeed = originalFlySpeed;
        this.disableHitCollision = disableHitCollision;
        this.horizontalVelocity = horizontalVelocity;
        this.arenaGameMode = arenaGameMode;
    }

    @Override
    public void setArenaState() {
        super.setArenaState();
        Player player = getPlayer();
        if (player == null) {
            return;
        }
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setGameMode(GameMode.ADVENTURE);
        player.setSwimming(false);
        if (this.disableHitCollision) {
            player.setCollidable(false);
        }

        // If playing on the inverted game-mode, negate the horizontal velocity to swap the controls
        if (this.arenaGameMode == DropperArenaGameMode.INVERTED) {
            player.setFlySpeed(-this.horizontalVelocity);
        } else {
            player.setFlySpeed(this.horizontalVelocity);
        }
    }

    @Override
    public boolean restore() {
        Player player = getPlayer();
        if (player == null) {
            return false;
        }
        this.restore(player);
        return true;
    }

    @Override
    public void restore(@NotNull Player player) {
        super.restore(player);
        player.setFlySpeed(this.originalFlySpeed);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = super.serialize();
        data.put("originalFlySpeed", this.originalFlySpeed);
        data.put("disableHitCollision", this.disableHitCollision);
        data.put("horizontalVelocity", this.horizontalVelocity);
        data.put("arenaGameMode", this.arenaGameMode);
        return data;
    }

    /**
     * Deserializes a ParkourPlayerEntryState from the given data
     *
     * @return <p>The data to deserialize</p>
     */
    @SuppressWarnings("unused")
    public static DropperPlayerEntryState deserialize(Map<String, Object> data) {
        UUID playerId = ((SerializableUUID) data.get("playerId")).getRawValue();
        boolean makePlayerInvisible = (boolean) data.get("makePlayerInvisible");
        Location entryLocation = (Location) data.get("entryLocation");
        boolean originalIsFlying = (boolean) data.get("originalIsFlying");
        GameMode originalGameMode = GameMode.valueOf((String) data.get("originalGameMode"));
        boolean originalAllowFlight = (boolean) data.get("originalAllowFlight");
        boolean originalInvulnerable = (boolean) data.get("originalInvulnerable");
        boolean originalIsSwimming = (boolean) data.get("originalIsSwimming");
        boolean originalCollideAble = (boolean) data.get("originalCollideAble");
        float originalFlySpeed = ((Number) data.get("originalFlySpeed")).floatValue();
        boolean disableHitCollision = (boolean) data.get("disableHitCollision");
        float horizontalVelocity = ((Number) data.get("horizontalVelocity")).floatValue();
        DropperArenaGameMode arenaGameMode = (DropperArenaGameMode) data.get("arenaGameMode");

        return new DropperPlayerEntryState(playerId, makePlayerInvisible, entryLocation, originalIsFlying,
                originalGameMode, originalAllowFlight, originalInvulnerable, originalIsSwimming, originalCollideAble,
                originalFlySpeed, disableHitCollision, horizontalVelocity, arenaGameMode);
    }

}
