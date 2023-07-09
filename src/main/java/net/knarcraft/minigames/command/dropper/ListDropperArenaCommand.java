package net.knarcraft.minigames.command.dropper;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.config.MiniGameMessage;
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
        StringBuilder builder = new StringBuilder(MiniGames.getInstance().getTranslator().getTranslatedMessage(
                MiniGameMessage.SUCCESS_DROPPER_ARENAS_LIST));
        for (String arenaName : TabCompleteHelper.getDropperArenas()) {
            builder.append("\n").append(arenaName);
        }
        MiniGames.getInstance().getStringFormatter().displaySuccessMessage(sender, builder.toString());
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] arguments) {
        return new ArrayList<>();
    }

}
