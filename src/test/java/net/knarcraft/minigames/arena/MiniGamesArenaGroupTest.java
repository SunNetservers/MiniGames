package net.knarcraft.minigames.arena;

import net.knarcraft.minigames.arena.dropper.DropperArenaGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Tests for arena dropper groups
 */
public class MiniGamesArenaGroupTest {

    @Test
    public void swapTest() {
        /*
           This test makes sure the order of arenas is as expected when the arenas are added to a group. It also makes
           sure that swapping two items works as expected.
         */

        DropperArenaGroup arenaGroup = new DropperArenaGroup("test");
        UUID arena1Id = UUID.randomUUID();
        UUID arena2Id = UUID.randomUUID();
        UUID arena3Id = UUID.randomUUID();
        UUID arena4Id = UUID.randomUUID();

        arenaGroup.addArena(arena1Id);
        arenaGroup.addArena(arena2Id);
        arenaGroup.addArena(arena3Id);
        arenaGroup.addArena(arena4Id);

        List<UUID> initialOrder = new ArrayList<>();
        initialOrder.add(arena1Id);
        initialOrder.add(arena2Id);
        initialOrder.add(arena3Id);
        initialOrder.add(arena4Id);
        Assertions.assertEquals(initialOrder, arenaGroup.getArenas());

        arenaGroup.swapArenas(1, 3);

        List<UUID> swapped = new ArrayList<>();
        swapped.add(arena1Id);
        swapped.add(arena4Id);
        swapped.add(arena3Id);
        swapped.add(arena2Id);
        Assertions.assertEquals(swapped, arenaGroup.getArenas());
    }

}
