package net.knarcraft.minigames.placeholder;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaGameMode;
import net.knarcraft.minigames.arena.dropper.DropperArenaGameMode;
import org.jetbrains.annotations.NotNull;

/**
 * A placeholder expansion for dropper record placeholders
 */
public class DropperRecordExpansion extends RecordExpansion {

    /**
     * Initializes a new record expansion
     *
     * @param plugin <p>A reference to the dropper plugin</p>
     */
    public DropperRecordExpansion(MiniGames plugin) {
        super(plugin.getDropperArenaHandler());
    }

    @Override
    public String getIdentifier() {
        return "dropper";
    }

    @Override
    protected @NotNull ArenaGameMode parseGameMode(@NotNull String gameMode) {
        return DropperArenaGameMode.matchGameMode(gameMode);
    }

}
