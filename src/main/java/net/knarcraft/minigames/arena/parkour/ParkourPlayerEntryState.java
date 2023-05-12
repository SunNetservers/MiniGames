package net.knarcraft.minigames.arena.parkour;

import net.knarcraft.minigames.arena.AbstractPlayerEntryState;
import net.knarcraft.minigames.container.SerializableUUID;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * The state of a player before entering a parkour arena
 */
public class ParkourPlayerEntryState extends AbstractPlayerEntryState {

    /**
     * Instantiates a new player state
     *
     * @param player <p>The player whose state should be stored</p>
     */
    public ParkourPlayerEntryState(@NotNull Player player) {
        super(player);
    }

    /**
     * Instantiates a new parkour player entry state
     *
     * @param playerId             <p>The id of the player whose state this should keep track of</p>
     * @param entryLocation        <p>The location the player entered from</p>
     * @param originalIsFlying     <p>Whether the player was flying before entering the arena</p>
     * @param originalGameMode     <p>The game-mode of the player before entering the arena</p>
     * @param originalAllowFlight  <p>Whether the player was allowed flight before entering the arena</p>
     * @param originalInvulnerable <p>Whether the player was invulnerable before entering the arena</p>
     * @param originalIsSwimming   <p>Whether the player was swimming before entering the arena</p>
     * @param originalCollideAble  <p>Whether the player was collide-able before entering the arena</p>
     */
    public ParkourPlayerEntryState(@NotNull UUID playerId, Location entryLocation,
                                   boolean originalIsFlying, GameMode originalGameMode, boolean originalAllowFlight,
                                   boolean originalInvulnerable, boolean originalIsSwimming, boolean originalCollideAble) {
        super(playerId, entryLocation, originalIsFlying, originalGameMode, originalAllowFlight,
                originalInvulnerable, originalIsSwimming, originalCollideAble);
    }

    @Override
    public void setArenaState() {
        Player player = getPlayer();
        if (player == null) {
            return;
        }
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setGameMode(GameMode.ADVENTURE);
        player.setSwimming(false);
    }

    /**
     * Deserializes a ParkourPlayerEntryState from the given data
     *
     * @return <p>The data to deserialize</p>
     */
    @SuppressWarnings("unused")
    public static ParkourPlayerEntryState deserialize(Map<String, Object> data) {
        UUID playerId = ((SerializableUUID) data.get("playerId")).getRawValue();
        Location entryLocation = (Location) data.get("entryLocation");
        boolean originalIsFlying = getBoolean(data, "originalIsFlying");
        GameMode originalGameMode = GameMode.valueOf((String) data.get("originalGameMode"));
        boolean originalAllowFlight = getBoolean(data, "originalAllowFlight");
        boolean originalInvulnerable = getBoolean(data, "originalInvulnerable");
        boolean originalIsSwimming = getBoolean(data, "originalIsSwimming");
        boolean originalCollideAble = getBoolean(data, "originalCollideAble");

        return new ParkourPlayerEntryState(playerId, entryLocation, originalIsFlying,
                originalGameMode, originalAllowFlight, originalInvulnerable, originalIsSwimming, originalCollideAble);
    }

}
