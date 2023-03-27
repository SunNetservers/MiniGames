package net.knarcraft.dropper.command;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArenaSession;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The command used to leave the current dropper arena
 */
public class LeaveArenaCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command must be used by a player");
            return false;
        }

        DropperArenaSession existingSession = Dropper.getInstance().getPlayerRegistry().getArenaSession(
                player.getUniqueId());
        if (existingSession == null) {
            commandSender.sendMessage("You are not in a dropper arena!");
            return false;
        }

        existingSession.triggerQuit(false);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] arguments) {
        return new ArrayList<>();
    }

}
