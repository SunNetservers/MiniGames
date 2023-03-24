package net.knarcraft.dropper.command;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArenaSession;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The command used to leave the current dropper arena
 */
public class LeaveArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command must be used by a player");
            return false;
        }

        DropperArenaSession existingSession = Dropper.getInstance().getPlayerRegistry().getArenaSession(player.getUniqueId());
        if (existingSession == null) {
            commandSender.sendMessage("You are not in a dropper arena!");
            return false;
        }

        existingSession.triggerQuit();
        return true;
    }

}
