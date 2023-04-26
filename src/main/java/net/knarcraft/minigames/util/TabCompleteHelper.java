package net.knarcraft.minigames.util;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.ArenaHandler;
import net.knarcraft.minigames.arena.EditablePropertyType;
import net.knarcraft.minigames.arena.dropper.DropperArenaEditableProperty;
import net.knarcraft.minigames.arena.parkour.ParkourArenaEditableProperty;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A helper-class for common tab-completions
 */
public final class TabCompleteHelper {

    private static Map<EditablePropertyType, List<String>> tabCompleteSuggestions;

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

    /**
     * Gets tab-complete suggestions for the given property type
     *
     * @param propertyType <p>The property type to get suggestions for</p>
     * @return <p>The suggestions produced</p>
     */
    public static List<String> getTabCompleteSuggestions(EditablePropertyType propertyType) {
        if (tabCompleteSuggestions == null) {
            tabCompleteSuggestions = new HashMap<>();
            tabCompleteSuggestions.put(EditablePropertyType.LOCATION, getLocationSuggestions());
            tabCompleteSuggestions.put(EditablePropertyType.ARENA_NAME, getNameSuggestions());
            tabCompleteSuggestions.put(EditablePropertyType.HORIZONTAL_VELOCITY, getHorizontalVelocitySuggestions());
            tabCompleteSuggestions.put(EditablePropertyType.VERTICAL_VELOCITY, getVerticalVelocitySuggestions());
            tabCompleteSuggestions.put(EditablePropertyType.BLOCK_TYPE, getBlockTypeSuggestions());
            tabCompleteSuggestions.put(EditablePropertyType.CHECKPOINT_CLEAR, getCheckpointClearSuggestions());
            tabCompleteSuggestions.put(EditablePropertyType.MATERIAL_LIST, getMaterialListSuggestions());
        }

        return tabCompleteSuggestions.get(propertyType);
    }

    /**
     * Gets suggestions for a list of materials
     *
     * @return <p>A list of suggestions</p>
     */
    private static List<String> getMaterialListSuggestions() {
        List<String> suggestions = new ArrayList<>();
        suggestions.add("LAVA,MAGMA_BLOCK");
        suggestions.add("WATER,MAGMA_BLOCK,LAVA,+BUTTONS,+CORALS");
        return suggestions;
    }

    /**
     * Gets suggestions for checkpointClear
     *
     * @return <p>A list of suggestions</p>
     */
    private static List<String> getCheckpointClearSuggestions() {
        List<String> suggestions = new ArrayList<>();
        suggestions.add("true");
        return suggestions;
    }

    /**
     * Gets suggestions for a block material
     *
     * @return <p>A list of suggestions</p>
     */
    private static List<String> getBlockTypeSuggestions() {
        List<String> materials = new ArrayList<>();
        for (Material material : Material.values()) {
            if (material.isBlock()) {
                materials.add(material.name());
            }
        }
        return materials;
    }

    /**
     * Gets suggestions for a vertical velocity
     *
     * @return <p>A list of suggestions</p>
     */
    private static List<String> getVerticalVelocitySuggestions() {
        List<String> velocities = new ArrayList<>();
        velocities.add("0.01");
        velocities.add("0.5");
        velocities.add("1");
        velocities.add("3");
        velocities.add("10");
        velocities.add("30");
        velocities.add("75");
        return velocities;
    }

    /**
     * Gets suggestions for a horizontal velocity
     *
     * @return <p>A list of suggestions</p>
     */
    private static List<String> getHorizontalVelocitySuggestions() {
        List<String> velocities = new ArrayList<>();
        velocities.add("0.01");
        velocities.add("0.1");
        velocities.add("0.5");
        velocities.add("1");
        return velocities;
    }

    /**
     * Gets suggestions for an arena name
     *
     * @return <p>A list of suggestions</p>
     */
    private static List<String> getNameSuggestions() {
        List<String> locations = new ArrayList<>();
        locations.add("DropperArena1");
        locations.add("DropperArena2");
        locations.add("ParkourArena1");
        locations.add("ParkourArena2");
        return locations;
    }

    /**
     * Gets suggestions for a location
     *
     * @return <p>A list of suggestions</p>
     */
    private static List<String> getLocationSuggestions() {
        List<String> locations = new ArrayList<>();
        locations.add("here");
        locations.add("x,y,z");
        return locations;
    }

}
