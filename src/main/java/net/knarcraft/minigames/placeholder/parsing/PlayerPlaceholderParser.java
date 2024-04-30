package net.knarcraft.minigames.placeholder.parsing;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.ArenaGameMode;
import net.knarcraft.minigames.arena.ArenaGroup;
import net.knarcraft.minigames.arena.ArenaHandler;
import net.knarcraft.minigames.arena.ArenaPlayerRegistry;
import net.knarcraft.minigames.arena.ArenaSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Level;

/**
 * A parser for player-related placeholders
 */
public class PlayerPlaceholderParser<K extends Arena> {

    private final @NotNull ArenaHandler<K, ?> arenaHandler;
    private final @NotNull Function<String, ArenaGameMode> gameModeParser;
    private final ArenaPlayerRegistry<K> playerRegistry;

    /**
     * Initializes a new player placeholder parser
     *
     * @param arenaHandler   <p>The arena handler to get arenas from</p>
     * @param gameModeParser <p>The function to use for parsing the specified game-mode</p>
     * @param playerRegistry <p>The player registry to get player info from</p>
     */
    public PlayerPlaceholderParser(@NotNull ArenaHandler<K, ?> arenaHandler,
                                   @NotNull Function<String, ArenaGameMode> gameModeParser,
                                   ArenaPlayerRegistry<K> playerRegistry) {
        this.arenaHandler = arenaHandler;
        this.gameModeParser = gameModeParser;
        this.playerRegistry = playerRegistry;
    }

    /**
     * The method to run when parsing a record placeholder request
     *
     * @param parts <p>The split parameters, without irrelevant info</p>
     * @return <p>The resulting string</p>
     */
    @Nullable
    public String onRequest(@NotNull String[] parts) {
        if (parts.length < 2) {
            return null;
        }

        String selector = parts[1];
        if (parts.length >= 6 && selector.equalsIgnoreCase("playing")) {
            return getPlayingPlayersInfo(parts);
        } else if (parts.length >= 3 && selector.equalsIgnoreCase("maximum")) {
            return getMaxPlayersInfo(parts);
        } else {
            return null;
        }
    }

    /**
     * Gets placeholder info about max players
     *
     * @param parts <p>The split parameters, without irrelevant info</p>
     * @return <p>The resulting string</p>
     */
    @Nullable
    private String getMaxPlayersInfo(@NotNull String[] parts) {
        String info = null;
        K arena = arenaHandler.getArena(parts[2]);
        if (arena != null) {
            info = String.valueOf(arena.getMaxPlayers());
        }
        return info;
    }

    /**
     * Gets placeholder info about playing players
     *
     * @param parts <p>The split parameters, without irrelevant info</p>
     * @return <p>The resulting string</p>
     */
    @Nullable
    private String getPlayingPlayersInfo(@NotNull String[] parts) {
        String gameModeName = parts[2];
        ArenaGameMode gameMode = gameModeParser.apply(gameModeName);
        if (gameModeName.equalsIgnoreCase("combined") || gameModeName.equalsIgnoreCase("all")) {
            gameMode = null;
        }

        SelectionType selectionType = SelectionType.getFromString(parts[3]);
        String identifier = parts[4];

        // The type of info to get. Either count (number of players) or player_position (a named player).
        PlayerInfoType infoType = PlayerInfoType.getFromString(parts[5]);
        if (infoType == null) {
            return null;
        }

        String info = null;
        if (selectionType == SelectionType.GROUP) {
            ArenaGroup<?, ?> group = arenaHandler.getGroup(identifier);
            if (group != null) {
                info = getGroupInfo(group, gameMode, infoType, parts);
            }
        } else if (selectionType == SelectionType.ARENA) {
            info = getArenaInfo(identifier, gameMode, infoType, parts);
        }

        return info;
    }

    /**
     * Gets information about an arena group's players
     *
     * @param group    <p>The group to get info about</p>
     * @param gameMode <p>The game-mode to get information for</p>
     * @param infoType <p>The type of information to get</p>
     * @param parts    <p>The placeholder arguments specified by a user</p>
     * @return <p>The specified group info, or null if the placeholder is invalid</p>
     */
    @Nullable
    private String getGroupInfo(@NotNull ArenaGroup<?, ?> group, @Nullable ArenaGameMode gameMode,
                                @NotNull PlayerInfoType infoType, @NotNull String[] parts) {
        List<UUID> arenaIds = group.getArenas();
        List<K> arenas = new ArrayList<>();
        for (UUID arenaId : arenaIds) {
            K arena = arenaHandler.getArena(arenaId);
            if (arena != null) {
                arenas.add(arena);
            }
        }

        if (infoType == PlayerInfoType.COUNT) {
            int playerCount = 0;
            for (K arena : arenas) {
                playerCount += getArenaPlayers(arena, gameMode).size();
            }
            return String.valueOf(playerCount);
        } else if (infoType == PlayerInfoType.PLAYER) {
            if (parts.length < 7) {
                return null;
            }
            Integer playerNumber = getPositionNumber(parts[6]);

            List<String> arenaPlayerNames = new ArrayList<>();
            for (K arena : arenas) {
                arenaPlayerNames.addAll(getArenaPlayersSorted(arena, gameMode));
            }
            arenaPlayerNames.sort(Comparator.naturalOrder());
            if (playerNumber != null) {
                if (playerNumber >= arenaPlayerNames.size()) {
                    return "";
                } else {
                    return arenaPlayerNames.get(playerNumber);
                }
            }
        }
        return null;
    }

    /**
     * Gets information about an arena's players
     *
     * @param arenaName <p>The name of the arena</p>
     * @param gameMode  <p>The game-mode to get information for</p>
     * @param infoType  <p>The type of information to get</p>
     * @param parts     <p>The placeholder arguments specified by a user</p>
     * @return <p>The specified arena info, or null if the placeholder is invalid</p>
     */
    @Nullable
    private String getArenaInfo(@NotNull String arenaName, @Nullable ArenaGameMode gameMode,
                                @NotNull PlayerInfoType infoType, @NotNull String[] parts) {
        if (infoType == PlayerInfoType.COUNT) {
            Set<UUID> arenaPlayers = getArenaPlayers(arenaName, gameMode);
            if (arenaPlayers != null) {
                return String.valueOf(arenaPlayers.size());
            }
        } else if (infoType == PlayerInfoType.PLAYER) {
            if (parts.length < 7) {
                return null;
            }
            Integer playerNumber = getPositionNumber(parts[6]);
            List<String> players = getArenaPlayersSorted(arenaName, gameMode);
            if (playerNumber != null && players != null) {
                if (playerNumber >= players.size()) {
                    return "";
                } else {
                    return players.get(playerNumber);
                }
            }
        }
        return null;
    }

    /**
     * Gets the position number from the given string
     *
     * @param positionNumber <p>The position number to parse</p>
     * @return <p>The position number, or null if not valid</p>
     */
    @Nullable
    private Integer getPositionNumber(@NotNull String positionNumber) {
        try {
            return Integer.parseInt(positionNumber) - 1;
        } catch (NumberFormatException exception) {
            MiniGames.log(Level.WARNING, "Invalid placeholder given. " + positionNumber +
                    " supplied instead of player number.");
            return null;
        }
    }

    /**
     * Gets names of all players in an arena in sorted order
     *
     * @param arenaName     <p>The name of the arena to get players from</p>
     * @param arenaGameMode <p>The game-mode to get players for</p>
     * @return <p>Player names in sorted order, or null if the arena name is invalid</p>
     */
    @Nullable
    private List<String> getArenaPlayersSorted(@NotNull String arenaName, @Nullable ArenaGameMode arenaGameMode) {
        K arena = arenaHandler.getArena(arenaName);
        if (arena == null) {
            return null;
        }
        return getArenaPlayersSorted(arena, arenaGameMode);
    }

    /**
     * Gets names of all players in an arena in sorted order
     *
     * @param arena         <p>The arena to get players from</p>
     * @param arenaGameMode <p>The game-mode to get players for</p>
     * @return <p>Player names in sorted order, or null if the arena name is invalid</p>
     */
    @NotNull
    private List<String> getArenaPlayersSorted(@NotNull K arena, @Nullable ArenaGameMode arenaGameMode) {
        Set<UUID> players = getArenaPlayers(arena, arenaGameMode);
        List<String> playerNames = new ArrayList<>(players.size());
        for (UUID playerId : players) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                playerNames.add(player.getName());
            }
        }
        playerNames.sort(Comparator.naturalOrder());
        return playerNames;
    }

    /**
     * Gets all players from the given arena
     *
     * @param arenaName     <p>The name of the arena to get players from</p>
     * @param arenaGameMode <p>The game-mode to get players for</p>
     * @return <p>The players in the given arena playing the given game-mode</p>
     */
    @Nullable
    private Set<UUID> getArenaPlayers(@NotNull String arenaName, @Nullable ArenaGameMode arenaGameMode) {
        K arena = arenaHandler.getArena(arenaName);
        if (arena == null) {
            return null;
        }

        return getArenaPlayers(arena, arenaGameMode);
    }

    /**
     * Gets all players from the given arena
     *
     * @param arena         <p>The arena to get players from</p>
     * @param arenaGameMode <p>The game-mode to get players for</p>
     * @return <p>The players in the given arena playing the given game-mode</p>
     */
    @NotNull
    private Set<UUID> getArenaPlayers(@NotNull K arena, @Nullable ArenaGameMode arenaGameMode) {
        // If getting count for any game-mode, skip filtering
        Set<UUID> players = playerRegistry.getPlayingPlayers(arena);
        if (arenaGameMode == null) {
            return players;
        }

        Set<UUID> output = new HashSet<>();
        for (UUID playerId : players) {
            ArenaSession arenaSession = playerRegistry.getArenaSession(playerId);
            if (arenaSession == null || arenaSession.getGameMode() != arenaGameMode) {
                continue;
            }

            output.add(playerId);
        }
        return output;
    }

}
