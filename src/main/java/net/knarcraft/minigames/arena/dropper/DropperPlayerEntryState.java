package net.knarcraft.minigames.arena.dropper;

import net.knarcraft.minigames.arena.AbstractPlayerEntryState;
import net.knarcraft.minigames.container.SerializableUUID;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * The state of a player before entering a dropper arena
 */
public class DropperPlayerEntryState extends AbstractPlayerEntryState {

    private final float originalFlySpeed;
    private final float horizontalVelocity;
    private final DropperArenaGameMode arenaGameMode;

    /**
     * Instantiates a new player state
     *
     * @param player <p>The player whose state should be stored</p>
     */
    public DropperPlayerEntryState(@NotNull Player player, @NotNull DropperArenaGameMode arenaGameMode,
                                   float horizontalVelocity) {
        super(player);
        this.originalFlySpeed = player.getFlySpeed();
        this.arenaGameMode = arenaGameMode;
        this.horizontalVelocity = horizontalVelocity;
    }

    /**
     * Instantiates a new parkour player entry state
     *
     * @param playerId              <p>The id of the player whose state this should keep track of</p>
     * @param entryLocation         <p>The location the player entered from</p>
     * @param originalIsFlying      <p>Whether the player was flying before entering the arena</p>
     * @param originalGameMode      <p>The game-mode of the player before entering the arena</p>
     * @param originalAllowFlight   <p>Whether the player was allowed flight before entering the arena</p>
     * @param originalInvulnerable  <p>Whether the player was invulnerable before entering the arena</p>
     * @param originalIsSwimming    <p>Whether the player was swimming before entering the arena</p>
     * @param originalFlySpeed      <p>The fly-speed of the player before entering the arena</p>
     * @param horizontalVelocity    <p>The horizontal velocity of the player before entering the arena</p>
     * @param originalCollideAble   <p>Whether the player was collide-able before entering the arena</p>
     * @param originalPotionEffects <p>The potion effects applied to the player when joining</p>
     * @param originalHealth        <p>The health of the player when joining the arena</p>
     */
    public DropperPlayerEntryState(@NotNull UUID playerId, @NotNull Location entryLocation,
                                   boolean originalIsFlying, GameMode originalGameMode, boolean originalAllowFlight,
                                   boolean originalInvulnerable, boolean originalIsSwimming,
                                   float originalFlySpeed, float horizontalVelocity,
                                   @NotNull DropperArenaGameMode arenaGameMode, boolean originalCollideAble,
                                   @NotNull Collection<PotionEffect> originalPotionEffects, double originalHealth) {
        super(playerId, entryLocation, originalIsFlying, originalGameMode, originalAllowFlight,
                originalInvulnerable, originalIsSwimming, originalCollideAble, originalPotionEffects, originalHealth);
        this.originalFlySpeed = originalFlySpeed;
        this.horizontalVelocity = horizontalVelocity;
        this.arenaGameMode = arenaGameMode;
    }

    @Override
    public void setArenaState() {
        Player player = getPlayer();
        if (player == null) {
            return;
        }
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setGameMode(GameMode.ADVENTURE);
        player.setSwimming(false);

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
        data.put("horizontalVelocity", this.horizontalVelocity);
        data.put("arenaGameMode", this.arenaGameMode);
        return data;
    }

    /**
     * Deserializes a ParkourPlayerEntryState from the given data
     *
     * @return <p>The data to deserialize</p>
     */
    @SuppressWarnings({"unused", "unchecked"})
    public static DropperPlayerEntryState deserialize(Map<String, Object> data) {
        UUID playerId = ((SerializableUUID) data.get("playerId")).getRawValue();
        Location entryLocation = (Location) data.get("entryLocation");
        boolean originalIsFlying = getBoolean(data, "originalIsFlying");
        GameMode originalGameMode = GameMode.valueOf((String) data.get("originalGameMode"));
        boolean originalAllowFlight = getBoolean(data, "originalAllowFlight");
        boolean originalInvulnerable = getBoolean(data, "originalInvulnerable");
        boolean originalIsSwimming = getBoolean(data, "originalIsSwimming");
        float originalFlySpeed = ((Number) data.get("originalFlySpeed")).floatValue();
        float horizontalVelocity = ((Number) data.get("horizontalVelocity")).floatValue();
        DropperArenaGameMode arenaGameMode = (DropperArenaGameMode) data.get("arenaGameMode");
        boolean originalCollideAble = getBoolean(data, "originalCollideAble");
        Collection<PotionEffect> originalPotionEffect =
                (Collection<PotionEffect>) data.getOrDefault("originalPotionEffects", new ArrayList<>());
        double originalHealth = ((Number) data.get("originalHealth")).doubleValue();

        return new DropperPlayerEntryState(playerId, entryLocation, originalIsFlying,
                originalGameMode, originalAllowFlight, originalInvulnerable, originalIsSwimming,
                originalFlySpeed, horizontalVelocity, arenaGameMode, originalCollideAble, originalPotionEffect,
                originalHealth);
    }

}
