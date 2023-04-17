package net.knarcraft.minigames.util;

import org.jetbrains.annotations.NotNull;

/**
 * A helper-class for sanitizing strings
 */
public final class StringSanitizer {

    private StringSanitizer() {

    }

    /**
     * Removes unwanted characters from a string
     *
     * <p>This basically removes character that have a special meaning in YML, or ones that cannot be used in the
     * chat.</p>
     *
     * @param input <p>The string to remove from</p>
     * @return <p>The string with the unwanted characters removed</p>
     */
    public static @NotNull String removeUnwantedCharacters(@NotNull String input) {
        return input.replaceAll("[§ :=&]", "");
    }

    /**
     * Sanitizes an arena name for usage as a YAML key
     *
     * @param arenaName <p>The arena name to sanitize</p>
     * @return <p>The sanitized arena name</p>
     */
    public static @NotNull String sanitizeArenaName(@NotNull String arenaName) {
        return arenaName.toLowerCase().trim().replaceAll(" ", "_");
    }

}
