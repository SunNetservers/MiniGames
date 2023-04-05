package net.knarcraft.dropper.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import net.knarcraft.dropper.Dropper;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class RecordExpansion extends PlaceholderExpansion implements Relational {

    private final Dropper plugin;

    public RecordExpansion(Dropper plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "dropper";
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
        /*String[] parts = parameters.split("_");
        // Record is used as the prefix for all record placeholders in case more placeholder types are added
        if (parts.length < 7 || !parts[0].equals("record")) {
            return parameters;
        }
        boolean timeRecords = parts[1].equals("time");
        ArenaGameMode gameMode = ArenaGameMode.matchGamemode(parts[2]);
        boolean isGroup = parts[3].equalsIgnoreCase("group");
        String identifier = parts[4];
        int recordNumber = Integer.parseInt(parts[5]);
        boolean value = parts[6].equalsIgnoreCase("value");

        DropperArenaHandler arenaHandler = Dropper.getInstance().getArenaHandler();
        if (isGroup) {
            DropperArenaGroup group = arenaHandler.getGroup(identifier);
        } else {
            DropperArena arena = arenaHandler.getArena(identifier);
            if (arena == null) {
                return parameters;
            }
            @NotNull Map<ArenaGameMode, DropperArenaRecordsRegistry> registries = arena.getData().recordRegistries();
            DropperArenaRecordsRegistry recordsRegistry = registries.get(gameMode);
            if (timeRecords) {
                recordsRegistry.getShortestTimeMilliSecondsRecords();
            } else {
                recordsRegistry.getLeastDeathsRecords();
            }
        }*/
        
        /*
        Format:
        %dropper_record_time_random_arena_arenaname_1_player%
        %dropper_record_time_random_arena_arenaname_1_value%

        dropper_record: Denotes that it's a placeholder for a dropper record
        deaths/time: The type of record to get
        default/inverted/random: The game-mode to get the record for
        arena/group: Denoting if the following name is the name of an arena or an arena group
        1,2,3,...: The placing to get: 1 for first place, etc.
        player/value: Whether to get the name of the player, or the player's record
         */
        // TODO: Figure out how placeholders should work. %dropper_group_1% should display the top player of the arena group
        return parameters;
    }

    @Override
    public String onPlaceholderRequest(Player player1, Player player2, String parameters) {
        return null;
    }

}
