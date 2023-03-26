package net.knarcraft.dropper.command;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The command for creating a new dropper arena
 */
public class CreateArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command must be used by a player");
            return false;
        }

        // Abort if no name was specified
        if (arguments.length < 1) {
            return false;
        }

        DropperArena existingArena = Dropper.getInstance().getArenaHandler().getArena(arguments[0]);
        if (existingArena != null) {
            commandSender.sendMessage("There already exists a dropper arena with that name!");
            return false;
        }

        // Remove known characters that are likely to cause trouble if used in an arena name
        String arenaName = arguments[0].replaceAll("[§ :=&]", "");

        DropperArena arena = new DropperArena(arenaName, player.getLocation());
        Dropper.getInstance().getArenaHandler().addArena(arena);
        commandSender.sendMessage("The arena was successfully created!");
        return true;
    }

}
