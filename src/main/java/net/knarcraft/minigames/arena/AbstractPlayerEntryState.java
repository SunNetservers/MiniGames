package net.knarcraft.minigames.arena;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.container.SerializableUUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * An abstract representation of a player's entry state
 */
public abstract class AbstractPlayerEntryState implements PlayerEntryState {

    protected final UUID playerId;
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
        this.playerId = player.getUniqueId();
        this.makePlayerInvisible = makePlayerInvisible;
        this.entryLocation = player.getLocation().clone();
        this.originalIsFlying = player.isFlying();
        this.originalGameMode = player.getGameMode();
        this.originalAllowFlight = player.getAllowFlight();
        this.originalInvulnerable = player.isInvulnerable();
        this.originalIsSwimming = player.isSwimming();
        this.originalCollideAble = player.isCollidable();
    }

    /**
     * Instantiates a new abstract player entry state
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
    public AbstractPlayerEntryState(@NotNull UUID playerId, boolean makePlayerInvisible, Location entryLocation,
                                    boolean originalIsFlying, GameMode originalGameMode, boolean originalAllowFlight,
                                    boolean originalInvulnerable, boolean originalIsSwimming,
                                    boolean originalCollideAble) {
        this.playerId = playerId;
        this.makePlayerInvisible = makePlayerInvisible;
        this.entryLocation = entryLocation;
        this.originalIsFlying = originalIsFlying;
        this.originalGameMode = originalGameMode;
        this.originalAllowFlight = originalAllowFlight;
        this.originalInvulnerable = originalInvulnerable;
        this.originalIsSwimming = originalIsSwimming;
        this.originalCollideAble = originalCollideAble;
    }

    @Override
    public @NotNull UUID getPlayerId() {
        return this.playerId;
    }

    @Override
    public void setArenaState() {
        Player player = getPlayer();
        if (player == null) {
            return;
        }
        if (this.makePlayerInvisible) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                    PotionEffect.INFINITE_DURATION, 3));
        }
    }

    @Override
    public boolean restore() {
        Player player = getPlayer();
        if (player == null) {
            return false;
        }
        restore(player);
        return true;
    }

    @Override
    public void restore(@NotNull Player player) {
        player.setFlying(this.originalIsFlying);
        player.setGameMode(this.originalGameMode);
        player.setAllowFlight(this.originalAllowFlight);
        player.setInvulnerable(this.originalInvulnerable);
        player.setSwimming(this.originalIsSwimming);
        player.setCollidable(this.originalCollideAble);
        if (this.makePlayerInvisible) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

    @Override
    public Location getEntryLocation() {
        return this.entryLocation;
    }

    /**
     * Gets the player this entry state belongs to
     *
     * @return <p>The player, or null if not currently online</p>
     */
    protected Player getPlayer() {
        Player player = Bukkit.getOfflinePlayer(this.playerId).getPlayer();
        if (player == null) {
            MiniGames.log(Level.WARNING, "Unable to change state for player with id " + this.playerId +
                    " because the player was not found on the server.");
        }
        return player;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("playerId", new SerializableUUID(this.playerId));
        data.put("makePlayerInvisible", this.makePlayerInvisible);
        data.put("entryLocation", this.entryLocation);
        data.put("originalIsFlying", this.originalIsFlying);
        data.put("originalGameMode", this.originalGameMode.name());
        data.put("originalAllowFlight", this.originalAllowFlight);
        data.put("originalInvulnerable", this.originalInvulnerable);
        data.put("originalIsSwimming", this.originalIsSwimming);
        data.put("originalCollideAble", this.originalCollideAble);
        return data;
    }

}
