package net.knarcraft.minigames.command.parkour;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaSession;
import net.knarcraft.minigames.arena.parkour.ParkourArenaSession;
import net.knarcraft.minigames.config.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The command for returning to the previous checkpoint
 */
public class ParkourCheckpointCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Message.ERROR_PLAYER_ONLY.getMessage());
            return false;
        }

        ArenaSession session = MiniGames.getInstance().getSession(player.getUniqueId());
        if (session instanceof ParkourArenaSession) {
            session.triggerLoss();
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] strings) {
        return new ArrayList<>();
    }

}
