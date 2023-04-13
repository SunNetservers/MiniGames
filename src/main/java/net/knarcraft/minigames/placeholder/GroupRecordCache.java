package net.knarcraft.minigames.placeholder;

import net.knarcraft.minigames.arena.dropper.DropperArenaGameMode;
import net.knarcraft.minigames.arena.record.ArenaRecord;
import net.knarcraft.minigames.property.RecordType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A record for keeping track of records for a dropper group
 *
 * @param gameMode    <p>The game-mode this cache is storing records for</p>
 * @param recordType  <p>The type of record stored</p>
 * @param records     <p>The stored records</p>
 * @param createdTime <p>The time this cache was created</p>
 */
public record GroupRecordCache<K extends Comparable<K>>(@NotNull DropperArenaGameMode gameMode,
                                                        @NotNull RecordType recordType,
                                                        @NotNull Set<ArenaRecord<K>> records,
                                                        @NotNull Long createdTime) {
}
