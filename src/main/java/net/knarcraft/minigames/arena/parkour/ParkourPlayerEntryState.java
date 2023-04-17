package net.knarcraft.minigames.arena.parkour;

import net.knarcraft.minigames.arena.AbstractPlayerEntryState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The state of a player before entering a parkour arena
 */
public class ParkourPlayerEntryState extends AbstractPlayerEntryState {

    /**
     * Instantiates a new player state
     *
     * @param player <p>The player whose state should be stored</p>
     */
    public ParkourPlayerEntryState(@NotNull Player player, boolean makePlayerInvisible) {
        super(player, makePlayerInvisible);
    }

    @Override
    public void setArenaState() {
        super.setArenaState();
        this.player.setAllowFlight(false);
        this.player.setFlying(false);
        this.player.setGameMode(GameMode.ADVENTURE);
        this.player.setSwimming(false);
        this.player.setCollidable(false);
    }

}
