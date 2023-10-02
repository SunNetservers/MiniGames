package net.knarcraft.minigames.util;

import net.knarcraft.knarlib.util.TabCompletionHelper;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.ArenaHandler;
import net.knarcraft.minigames.arena.EditablePropertyType;
import net.knarcraft.minigames.arena.dropper.DropperArenaEditableProperty;
import net.knarcraft.minigames.arena.parkour.ParkourArenaEditableProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * A helper-class for common tab-completions
 */
public final class TabCompleteHelper {

    private static Map<EditablePropertyType, List<String>> tabCompleteSuggestions;
    private static List<String> plugins;
    private static Map<String, List<String>> permissions;

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
            tabCompleteSuggestions.put(EditablePropertyType.DOUBLE, getDoubleSuggestions());
        }

        return tabCompleteSuggestions.get(propertyType);
    }

    /**
     * Gets suggestions for double values
     *
     * @return <p>A list of suggestions</p>
     */
    private static List<String> getDoubleSuggestions() {
        List<String> suggestions = new ArrayList<>();
        suggestions.add("0");
        suggestions.add("0.01");
        suggestions.add("0.1");
        suggestions.add("0.2");
        suggestions.add("0.3");
        suggestions.add("0.4");
        suggestions.add("0.5");
        suggestions.add("1");
        return suggestions;
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
        suggestions.add("CHAIN,END_ROD,LIGHTNING_ROD");
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

    /**
     * Gets the tab complete value for the permission typed
     *
     * @param typedNode <p>The full permission node typed by the player</p>
     * @return <p>All known valid auto-complete options</p>
     */
    public static List<String> tabCompletePermission(String typedNode) {
        if (plugins == null) {
            loadAvailablePermissions();
        }
        List<String> output;
        if (typedNode.contains(".")) {
            List<String> matchingPermissions = permissions.get(typedNode.substring(0, typedNode.lastIndexOf(".")));
            if (matchingPermissions == null) {
                output = new ArrayList<>();
            } else {
                //Filter by the typed text
                output = TabCompletionHelper.filterMatchingStartsWith(matchingPermissions, typedNode);
            }
        } else {
            output = plugins;
        }

        //Add previous permissions in the comma-separated lists as a prefix
        return output;
    }

    /**
     * Loads all permissions available from bukkit plugins
     */
    private static void loadAvailablePermissions() {
        plugins = new ArrayList<>();
        permissions = new HashMap<>();

        for (Permission permission : Bukkit.getPluginManager().getPermissions()) {
            loadPermission(permission.getName());
        }
    }

    /**
     * Loads a given permission into the proper lists and maps
     *
     * @param permissionName <p>The permission to load</p>
     */
    private static void loadPermission(String permissionName) {
        String[] permissionParts = permissionName.split("\\.");
        if (permissionParts.length == 1 && !plugins.contains(permissionParts[0])) {
            plugins.add(permissionParts[0]);
        } else if (permissionParts.length > 1) {
            StringJoiner pathJoiner = new StringJoiner(".");
            for (int j = 0; j < permissionParts.length - 1; j++) {
                pathJoiner.add(permissionParts[j]);
            }
            String path = pathJoiner.toString();
            List<String> permissionList = permissions.computeIfAbsent(path, k -> new ArrayList<>());
            permissionList.add(permissionName);

            loadPermission(path);
        }
    }

}
