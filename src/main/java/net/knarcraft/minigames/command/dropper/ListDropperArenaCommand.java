package net.knarcraft.minigames.command.dropper;

import net.knarcraft.minigames.config.Message;
import net.knarcraft.minigames.util.TabCompleteHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A command for listing existing dropper arenas
 */
public class ListDropperArenaCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] arguments) {
        StringBuilder builder = new StringBuilder(Message.SUCCESS_DROPPER_ARENAS_LIST.getMessage());
        for (String arenaName : TabCompleteHelper.getDropperArenas()) {
            builder.append("\n").append(arenaName);
        }
        sender.sendMessage(builder.toString());
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] arguments) {
        return new ArrayList<>();
    }

}
