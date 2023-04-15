package net.knarcraft.minigames.command;

import net.knarcraft.minigames.arena.ArenaGameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * An abstract class for an arena joining tab-completer
 */
public abstract class JoinArenaTabCompleter implements TabCompleter {

    private final ArenaGameMode gameMode;
    private final Supplier<List<String>> arenaNameSupplier;

    /**
     * Implements a new join arena tab completer
     *
     * @param arenaNameSupplier <p>The supplier to ask for arena names</p>
     * @param gameMode          <p>An instance of one of the available game-modes</p>
     */
    public JoinArenaTabCompleter(Supplier<List<String>> arenaNameSupplier, ArenaGameMode gameMode) {
        this.arenaNameSupplier = arenaNameSupplier;
        this.gameMode = gameMode;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] arguments) {
        if (arguments.length == 1) {
            return arenaNameSupplier.get();
        } else if (arguments.length == 2) {
            List<String> gameModes = new ArrayList<>();
            for (ArenaGameMode gameMode : gameMode.getValues()) {
                gameModes.add(gameMode.name().toLowerCase());
            }
            return gameModes;
        } else {
            return new ArrayList<>();
        }
    }

}
