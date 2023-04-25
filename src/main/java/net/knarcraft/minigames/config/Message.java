package net.knarcraft.minigames.config;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.container.PlaceholderContainer;
import net.knarcraft.minigames.util.ColorHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * A message which ca be displayed to the user
 */
public enum Message {

    ERROR_CANNOT_SAVE_ARENA_GROUPS("&cUnable to save current arena groups! Data loss can occur!"),
    ERROR_MATERIAL_NOT_PARSE_ABLE("&cUnable to parse material: {material}"),
    ERROR_TELEPORT_WITH_PASSENGER("&cYou cannot be teleported with a passenger!"),
    ERROR_TELEPORT_IN_VEHICLE("&cYou cannot be teleported while in a vehicle"),
    ERROR_ARENA_NOT_LOADED("&cCould not load the arena at configuration section {section}. Please check " +
            "the {file} storage file for issues."),
    ERROR_ARENA_DATA_NOT_LOADED("&cUnable to load arena data for dropper arena: {arena}"),
    ERROR_ARENA_NOT_FOUND("&cUnable to find the specified arena."),
    ERROR_GROUP_NOT_FOUND("&cUnable to find the specified group!"),
    ERROR_PLAYER_ONLY("&cThis command must be used by a player"),
    ERROR_ARENA_NAME_COLLISION("&cThere already exists an arena with that name!"),
    ERROR_NORMAL_MODE_REQUIRED("&cYou must complete this arena in normal mode first!"),
    ERROR_GROUP_NORMAL_MODE_REQUIRED("&cYou have not yet beaten the default game-mode for all arenas in this group!"),
    ERROR_PREVIOUS_ARENA_REQUIRED("&cYou have not yet beaten the previous arena!"),
    ERROR_ARENA_TELEPORT_FAILED("&cUnable to teleport you to the arena."),
    ERROR_NOT_IN_ARENA("&cYou are not in a mini-games arena!"),
    ERROR_ALREADY_PLAYING("&cYou are already playing a mini-game!"),
    ERROR_JOIN_IN_VEHICLE_OR_PASSENGER("&cYou cannot join an arena while inside a vehicle or carrying a passenger."),
    ERROR_UNKNOWN_PROPERTY("&cUnknown property specified."),
    ERROR_PROPERTY_INPUT_INVALID("&cUnable to change the property. Make sure your input is valid!"),
    ERROR_ARENA_1_NOT_FOUND("&cUnable to find the first specified arena."),
    ERROR_ARENA_2_NOT_FOUND("&cUnable to find the second specified dropper arena."),
    ERROR_SWAP_DIFFERENT_GROUPS("&cYou cannot swap arenas in different groups!"),
    ERROR_ILLEGAL_COMMAND("&cYou cannot use that command while in an arena!"),
    SUCCESS_ARENA_GROUP_UPDATED("&aThe arena's group has been updated"),
    SUCCESS_PLUGIN_RELOADED("&aPlugin reloaded!"),
    SUCCESS_ARENA_CREATED("&aThe arena was successfully created!"),
    SUCCESS_ARENA_FIRST_CLEAR("&aYou cleared the arena!"),
    SUCCESS_ARENA_WIN("&aYou won!"),
    SUCCESS_ARENA_QUIT("&aYou quit the arena!"),
    SUCCESS_CURRENT_VALUE("&aCurrent value of {property} is: {value}"),
    SUCCESS_PROPERTY_CHANGED("&aProperty {property} successfully changed"),
    SUCCESS_ARENAS_SWAPPED("&aThe arenas have been swapped!"),
    SUCCESS_ARENA_REMOVED("&aThe specified arena has been successfully removed"),
    SUCCESS_DROPPER_ARENAS_LIST("&aDropper arenas:&r"),
    SUCCESS_PARKOUR_ARENAS_LIST("&aParkour arenas:&r"),
    SUCCESS_CHECKPOINT_REACHED("&aCheckpoint reached!"),
    SUCCESS_GROUP_STAGES("&a{group}'s stages:&r"),
    SUCCESS_RECORD_ACHIEVED("&aYou just set a {recordInfo} on the {gameMode} game-mode!"),
    RECORD_ACHIEVED_GLOBAL("new {recordType} record"),
    RECORD_ACHIEVED_PERSONAL("personal {recordType} record"),
    ;

    private final @NotNull String defaultMessage;

    /**
     * Instantiates a new message
     *
     * @param defaultMessage <p>The default value of the message</p>
     */
    Message(@NotNull String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    /**
     * Gets the message this enum represents
     *
     * @return <p>The formatted message</p>
     */
    public @NotNull String getMessage() {
        return formatMessage(this.defaultMessage);
    }

    /**
     * Gets the message this enum represents
     *
     * @param placeholder <p>The placeholder to replace</p>
     * @param replacement <p>The replacement to use</p>
     * @return <p>The formatted message</p>
     */
    public @NotNull String getMessage(@NotNull String placeholder, @NotNull String replacement) {
        return formatMessage(this.defaultMessage.replace(placeholder, replacement));
    }

    /**
     * Gets the message this enum represents
     *
     * @param placeholders <p>The placeholder -> replacement map specifying necessary replacements</p>
     * @return <p>The formatted message</p>
     */
    public @NotNull String getMessage(@NotNull PlaceholderContainer placeholders) {
        String replaced = this.defaultMessage;
        for (Map.Entry<String, String> entry : placeholders.getPlaceholders().entrySet()) {
            replaced = replaced.replace(entry.getKey(), entry.getValue());
        }
        return formatMessage(replaced);
    }

    /**
     * Gets the formatted version of the given message
     *
     * @param message <p>The message to format</p>
     * @return <p>The formatted message</p>
     */
    private @NotNull String formatMessage(@NotNull String message) {
        String prefix = MiniGames.getInstance().getDescription().getPrefix();
        return ColorHelper.translateAllColorCodes("#546EED[&r&l" + prefix + "#546EED]&r " + message);
    }

}
