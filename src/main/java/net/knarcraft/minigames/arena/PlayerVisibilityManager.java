package net.knarcraft.minigames.arena;

import net.knarcraft.minigames.MiniGames;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A manager for keeping track of which players have set other players as hidden
 */
public class PlayerVisibilityManager {

    private final Set<UUID> hidingEnabledFor = new HashSet<>();

    /**
     * Toggles whether players should be hidden for the player with the given id
     *
     * @param player <p>The the player to update</p>
     */
    public void toggleHidePlayers(@NotNull ArenaPlayerRegistry<?> playerRegistry, @NotNull Player player) {
        if (hidingEnabledFor.contains(player.getUniqueId())) {
            hidingEnabledFor.remove(player.getUniqueId());
            // Make all other players visible again
            changeVisibilityFor(playerRegistry, player, false);
        } else {
            hidingEnabledFor.add(player.getUniqueId());
            // Make all other players hidden
            changeVisibilityFor(playerRegistry, player, true);
        }

    }

    /**
     * Updates which players are seen as hidden
     *
     * @param playerRegistry <p>The registry containing all playing players</p>
     * @param player         <p>The player that joined the arena</p>
     */
    public void updateHiddenPlayers(@NotNull ArenaPlayerRegistry<?> playerRegistry, @NotNull Player player) {
        boolean hideForPlayer = hidingEnabledFor.contains(player.getUniqueId());
        for (UUID playerId : playerRegistry.getPlayingPlayers()) {
            Player otherPlayer = Bukkit.getPlayer(playerId);
            if (otherPlayer == null) {
                continue;
            }
            // Hide the arena player from the newly joined player
            if (hideForPlayer) {
                player.hidePlayer(MiniGames.getInstance(), otherPlayer);
            }
            // Hide the newly joined player from this player
            if (hidingEnabledFor.contains(playerId)) {
                otherPlayer.hidePlayer(MiniGames.getInstance(), player);
            }
        }
    }

    /**
     * Makes all players visible to the given player
     *
     * @param player <p>The player to update visibility for</p>
     */
    public void showPlayersFor(@NotNull Player player) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            player.showPlayer(MiniGames.getInstance(), otherPlayer);
            otherPlayer.showPlayer(MiniGames.getInstance(), player);
        }
    }

    /**
     * Changes whether the given player can see the other players in the arena
     *
     * @param playerRegistry <p>The player registry containing other players</p>
     * @param player         <p>The player to change the visibility for</p>
     * @param hide           <p>Whether to hide the players or show the players</p>
     */
    private void changeVisibilityFor(@NotNull ArenaPlayerRegistry<?> playerRegistry, @NotNull Player player, boolean hide) {
        for (UUID playerId : playerRegistry.getPlayingPlayers()) {
            Player otherPlayer = Bukkit.getPlayer(playerId);
            if (otherPlayer == null) {
                continue;
            }

            if (hide) {
                player.hidePlayer(MiniGames.getInstance(), otherPlayer);
            } else {
                player.showPlayer(MiniGames.getInstance(), otherPlayer);
            }
        }
    }

}
