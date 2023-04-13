package net.knarcraft.dropper.arena.dropper;

import net.knarcraft.dropper.MiniGames;
import net.knarcraft.dropper.arena.ArenaGroup;
import net.knarcraft.dropper.container.SerializableUUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A sorted group of arenas that must be completed in sequence
 */
public class DropperArenaGroup extends ArenaGroup {

    /**
     * Instantiates a new dropper arena group
     *
     * @param groupName <p>The name of this group</p>
     */
    public DropperArenaGroup(@NotNull String groupName) {
        super(groupName);
    }

    /**
     * Instantiates a new dropper arena group
     *
     * @param groupId   <p>The unique id of this group</p>
     * @param groupName <p>The name of this group</p>
     * @param arenas    <p>The arenas in this group</p>
     */
    private DropperArenaGroup(@NotNull UUID groupId, @NotNull String groupName, @NotNull List<UUID> arenas) {
        super(groupId, groupName, arenas);
    }

    /**
     * Checks whether the given player has beaten all arenas in this group on the given game-mode
     *
     * @param gameMode <p>The game-mode to check</p>
     * @param player   <p>The player to check</p>
     * @return <p>True if the player has beaten all arenas, false otherwise</p>
     */
    public boolean hasBeatenAll(DropperArenaGameMode gameMode, Player player) {
        DropperArenaHandler arenaHandler = MiniGames.getInstance().getDropperArenaHandler();
        for (UUID anArenaId : this.getArenas()) {
            DropperArena dropperArena = arenaHandler.getArena(anArenaId);
            if (dropperArena == null) {
                // The arena would only be null if the arena has been deleted, but not removed from this group
                MiniGames.log(Level.WARNING, "The dropper group " + this.getGroupName() +
                        " contains the arena id " + anArenaId + " which is not a valid arena id!");
                continue;
            }

            if (dropperArena.getData().hasNotCompleted(gameMode, player)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets whether the given player can play the given arena part of this group, on the given game-mode
     *
     * @param gameMode <p>The game-mode the player is trying to play</p>
     * @param player   <p>The player to check</p>
     * @param arenaId  <p>The id of the arena in this group to check</p>
     * @return <p>True if the player is allowed to play the arena</p>
     * @throws IllegalArgumentException <p>If checking an arena not in this group</p>
     */
    public boolean canPlay(DropperArenaGameMode gameMode, Player player, UUID arenaId) throws IllegalArgumentException {
        if (!this.arenas.contains(arenaId)) {
            throw new IllegalArgumentException("Cannot check for playability for arena not in this group!");
        }

        DropperArenaHandler arenaHandler = MiniGames.getInstance().getDropperArenaHandler();

        for (UUID anArenaId : this.getArenas()) {
            // If the target arena is reached, allow, as all previous arenas must have been cleared
            if (arenaId.equals(anArenaId)) {
                return true;
            }

            DropperArena dropperArena = arenaHandler.getArena(anArenaId);
            if (dropperArena == null) {
                // The arena would only be null if the arena has been deleted, but not removed from this group
                MiniGames.log(Level.WARNING, String.format("The dropper group %s contains the" +
                        " arena id %s which is not a valid arena id!", this.getGroupName(), anArenaId));
                continue;
            }

            // This is a lower-numbered arena the player has yet to complete
            if (dropperArena.getData().hasNotCompleted(gameMode, player)) {
                return false;
            }
        }

        return false;
    }

    /**
     * Deserializes the given data
     *
     * @param data <p>The data to deserialize</p>
     * @return <p>The deserialized arena group</p>
     */
    @SuppressWarnings({"unused", "unchecked"})
    public static @NotNull DropperArenaGroup deserialize(@NotNull Map<String, Object> data) {
        UUID id = ((SerializableUUID) data.get("groupId")).uuid();
        String name = (String) data.get("groupName");
        List<SerializableUUID> serializableArenas = (List<SerializableUUID>) data.get("arenas");
        List<UUID> arenas = new ArrayList<>();
        for (SerializableUUID arenaId : serializableArenas) {
            arenas.add(arenaId.uuid());
        }
        return new DropperArenaGroup(id, name, arenas);
    }

}
