package net.knarcraft.minigames.arena.parkour;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

/**
 * The state of a player before entering a dropper arena
 */
public class ParkourPlayerEntryState {

    private final Player player;
    private final Location entryLocation;
    private final boolean originalIsFlying;
    private final GameMode originalGameMode;
    private final boolean originalAllowFlight;
    private final boolean originalInvulnerable;
    private final boolean originalIsSwimming;
    private final boolean originalCollideAble;
    private final boolean makePlayerInvisible;

    /**
     * Instantiates a new player state
     *
     * @param player <p>The player whose state should be stored</p>
     */
    public ParkourPlayerEntryState(@NotNull Player player, boolean makePlayerInvisible) {
        this.player = player;
        this.entryLocation = player.getLocation().clone();
        this.originalIsFlying = player.isFlying();
        this.originalGameMode = player.getGameMode();
        this.originalAllowFlight = player.getAllowFlight();
        this.originalInvulnerable = player.isInvulnerable();
        this.originalIsSwimming = player.isSwimming();
        this.originalCollideAble = player.isCollidable();
        this.makePlayerInvisible = makePlayerInvisible;
    }

    /**
     * Sets the state of the stored player to the state used by arenas
     */
    public void setArenaState() {
        this.player.setAllowFlight(false);
        this.player.setFlying(false);
        this.player.setGameMode(GameMode.ADVENTURE);
        this.player.setSwimming(false);
        this.player.setCollidable(false);
        if (this.makePlayerInvisible) {
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                    PotionEffect.INFINITE_DURATION, 3));
        }
    }

    /**
     * Restores the stored state for the stored player
     */
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

    /**
     * Gets the location the player entered from
     *
     * @return <p>The location the player entered from</p>
     */
    public Location getEntryLocation() {
        return this.entryLocation;
    }

}
