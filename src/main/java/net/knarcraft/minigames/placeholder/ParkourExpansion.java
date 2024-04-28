package net.knarcraft.minigames.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGameMode;
import net.knarcraft.minigames.arena.parkour.ParkourArenaHandler;
import net.knarcraft.minigames.placeholder.parsing.PlayerPlaceholderParser;
import net.knarcraft.minigames.placeholder.parsing.RecordPlaceholderParser;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * A placeholderAPI expansion for Parkour-related placeholders
 */
public class ParkourExpansion extends PlaceholderExpansion {

    private final @NotNull RecordPlaceholderParser recordPlaceholderParser;
    private final @NotNull PlayerPlaceholderParser<ParkourArena> playerPlaceholderParser;

    /**
     * Instantiates a new dropper expansion
     *
     * @param plugin <p>A reference to the mini-games plugin</p>
     */
    public ParkourExpansion(@NotNull MiniGames plugin) {
        ParkourArenaHandler arenaHandler = plugin.getParkourArenaHandler();
        this.recordPlaceholderParser = new RecordPlaceholderParser(arenaHandler, ParkourArenaGameMode::matchGamemode);
        this.playerPlaceholderParser = new PlayerPlaceholderParser<>(arenaHandler, ParkourArenaGameMode::matchGamemode,
                plugin.getParkourArenaPlayerRegistry());
    }

    /**
     * Clears record caches
     */
    public void clearCaches() {
        this.recordPlaceholderParser.clearCaches();
    }

    @Override
    public String getIdentifier() {
        return "parkour";
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
    public String onRequest(OfflinePlayer player, String parameters) {
        String[] parts = parameters.split("_");
        // Record is used as the prefix for all record placeholders in case more placeholder types are added
        if (parts[0].equalsIgnoreCase("record") && parts.length >= 7) {
            return recordPlaceholderParser.onRequest(parameters, parts);
        } else if (parts[0].equalsIgnoreCase("players")) {
            return this.playerPlaceholderParser.onRequest(parameters, parts);
        } else {
            return parameters;
        }
    }

}
