package net.knarcraft.minigames.util;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.ArenaHandler;
import net.knarcraft.minigames.arena.dropper.DropperArenaEditableProperty;
import net.knarcraft.minigames.arena.parkour.ParkourArenaEditableProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A helper-class for common tab-completions
 */
public final class TabCompleteHelper {

    private TabCompleteHelper() {

    }

    /**
     * Gets the names of all current arenas
     *
     * @return <p>All arena names</p>
     */
    public static @NotNull List<String> getDropperArenas() {
        return getArenas(MiniGames.getInstance().getDropperArenaHandler());
    }

    /**
     * Gets the names of all current arenas
     *
     * @return <p>All arena names</p>
     */
    public static @NotNull List<String> getParkourArenas() {
        return getArenas(MiniGames.getInstance().getParkourArenaHandler());
    }

    /**
     * Gets the names of all current arenas
     *
     * @return <p>All arena names</p>
     */
    private static @NotNull List<String> getArenas(ArenaHandler<?, ?> arenaHandler) {
        List<String> arenaNames = new ArrayList<>();
        for (Arena arena : arenaHandler.getArenas().values()) {
            arenaNames.add(arena.getArenaName());
        }
        return arenaNames;
    }

    /**
     * Gets the argument strings of all arena properties
     *
     * @return <p>All arena properties</p>
     */
    public static @NotNull List<String> getDropperArenaProperties() {
        List<String> arenaProperties = new ArrayList<>();
        for (DropperArenaEditableProperty property : DropperArenaEditableProperty.values()) {
            arenaProperties.add(property.getArgumentString());
        }
        return arenaProperties;
    }

    /**
     * Gets the argument strings of all arena properties
     *
     * @return <p>All arena properties</p>
     */
    public static @NotNull List<String> getParkourArenaProperties() {
        List<String> arenaProperties = new ArrayList<>();
        for (ParkourArenaEditableProperty property : ParkourArenaEditableProperty.values()) {
            arenaProperties.add(property.getArgumentString());
        }
        return arenaProperties;
    }

    /**
     * Finds tab complete values that contain the typed text
     *
     * @param values    <p>The values to filter</p>
     * @param typedText <p>The text the player has started typing</p>
     * @return <p>The given string values that contain the player's typed text</p>
     */
    public static List<String> filterMatchingContains(@NotNull List<String> values, @NotNull String typedText) {
        List<String> configValues = new ArrayList<>();
        for (String value : values) {
            if (value.toLowerCase().contains(typedText.toLowerCase())) {
                configValues.add(value);
            }
        }
        return configValues;
    }

}
