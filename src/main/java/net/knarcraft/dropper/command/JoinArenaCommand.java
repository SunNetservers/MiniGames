package net.knarcraft.dropper.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The command used to join a dropper arena
 */
public class JoinArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command must be used by a player");
            return false;
        }
        //TODO: Implement command behavior
        //TODO: Remember to check if the player is already in an arena first!
        return true;
    }

}
