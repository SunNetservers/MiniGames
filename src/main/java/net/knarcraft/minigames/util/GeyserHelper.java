package net.knarcraft.minigames.util;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.geyser.api.GeyserApi;
import org.jetbrains.annotations.NotNull;

/**
 * A helper class for dealing with geyser/floodgate players
 */
public final class GeyserHelper {

    private static boolean hasGeyser = true;
    private static boolean hasFloodgate = true;

    private GeyserHelper() {

    }

    /**
     * Checks whether the given player is connected through Geyser
     *
     * @param player <p>The player to check</p>
     * @return <p>True if the player is connected through Geyser</p>
     */
    public static boolean isGeyserPlayer(@NotNull Player player) {
        // Prevent unnecessary checking for non-geyser and floodgate servers
        if (!hasGeyser && !hasFloodgate) {
            return false;
        }

        // Use Geyser API to get connection status
        if (hasGeyser) {
            try {
                return GeyserApi.api().connectionByUuid(player.getUniqueId()) != null;
            } catch (NoClassDefFoundError error1) {
                hasGeyser = false;
            }
        }

        // Use Floodgate API to get connection status
        if (hasFloodgate) {
            try {
                return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
            } catch (NoClassDefFoundError error2) {
                hasFloodgate = false;
            }
        }

        return false;
    }

}
