package net.knarcraft.minigames.placeholder;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaGameMode;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGameMode;
import org.jetbrains.annotations.NotNull;

/**
 * A placeholder expansion for parkour record placeholders
 */
public class ParkourRecordExpansion extends RecordExpansion {

    /**
     * Initializes a new record expansion
     *
     * @param plugin <p>A reference to the dropper plugin</p>
     */
    public ParkourRecordExpansion(MiniGames plugin) {
        super(plugin.getParkourArenaHandler());
    }

    @Override
    public String getIdentifier() {
        return "parkour";
    }

    @Override
    protected @NotNull ArenaGameMode parseGameMode(@NotNull String gameMode) {
        return ParkourArenaGameMode.matchGamemode(gameMode);
    }

}
