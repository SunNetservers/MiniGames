package net.knarcraft.minigames.command;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.config.MiniGameMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The command for reloading the plugin
 */
public class ReloadCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        MiniGames.getInstance().reload();
        MiniGames.getInstance().getStringFormatter().displaySuccessMessage(commandSender,
                MiniGameMessage.SUCCESS_PLUGIN_RELOADED);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] arguments) {
        return new ArrayList<>();
    }

}
