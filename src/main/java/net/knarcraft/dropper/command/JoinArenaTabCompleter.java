package net.knarcraft.dropper.command;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The tab-completer for the join command
 */
public class JoinArenaTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> arenaNames = new ArrayList<>();
            for (DropperArena dropperArena : Dropper.getInstance().getArenaHandler().getArenas()) {
                arenaNames.add(dropperArena.getArenaName());
            }
            return arenaNames;
        } else if (args.length == 2) {
            List<String> gameModes = new ArrayList<>();
            gameModes.add("default");
            gameModes.add("deaths");
            gameModes.add("time");
            return gameModes;
        } else {
            return new ArrayList<>();
        }
    }

}
