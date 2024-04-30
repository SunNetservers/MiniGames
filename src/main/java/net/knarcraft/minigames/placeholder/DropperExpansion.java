package net.knarcraft.minigames.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.dropper.DropperArena;
import net.knarcraft.minigames.arena.dropper.DropperArenaGameMode;
import net.knarcraft.minigames.arena.dropper.DropperArenaHandler;
import net.knarcraft.minigames.placeholder.parsing.PlayerPlaceholderParser;
import net.knarcraft.minigames.placeholder.parsing.RecordPlaceholderParser;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A placeholderAPI expansion for Dropper-related placeholders
 */
public class DropperExpansion extends PlaceholderExpansion {

    private final @NotNull RecordPlaceholderParser recordPlaceholderParser;
    private final @NotNull PlayerPlaceholderParser<DropperArena> playerPlaceholderParser;

    /**
     * Instantiates a new dropper expansion
     *
     * @param plugin <p>A reference to the mini-games plugin</p>
     */
    public DropperExpansion(@NotNull MiniGames plugin) {
        DropperArenaHandler arenaHandler = plugin.getDropperArenaHandler();
        this.recordPlaceholderParser = new RecordPlaceholderParser(arenaHandler, DropperArenaGameMode::matchGameMode);
        this.playerPlaceholderParser = new PlayerPlaceholderParser<>(arenaHandler, DropperArenaGameMode::matchGameMode,
                plugin.getDropperArenaPlayerRegistry());
    }

    /**
     * Clears record caches
     */
    public void clearCaches() {
        this.recordPlaceholderParser.clearCaches();
    }

    @Override
    public String getIdentifier() {
        return "dropper";
    }

    @Override
    public String getAuthor() {
        return "EpicKnarvik97";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    @Nullable
    public String onRequest(OfflinePlayer player, String parameters) {
        String[] parts = parameters.split("_");
        // Record is used as the prefix for all record placeholders in case more placeholder types are added
        if (parts[0].equalsIgnoreCase("record") && parts.length >= 7) {
            return recordPlaceholderParser.onRequest(parts);
        } else if (parts[0].equalsIgnoreCase("players")) {
            return this.playerPlaceholderParser.onRequest(parts);
        } else {
            return parameters;
        }
    }

}
