package net.knarcraft.minigames.listener;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * A listener for players trying to use commands while inside a dropper arena
 */
public class CommandListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        ArenaSession existingSession = MiniGames.getInstance().getSession(player.getUniqueId());
        if (existingSession == null) {
            return;
        }

        List<String> allowedCommands = new ArrayList<>();
        allowedCommands.add("/miniGamesLeave");
        allowedCommands.add("/mLeave");

        String message = event.getMessage();
        if (!message.startsWith("/")) {
            return;
        }

        for (String command : allowedCommands) {
            if (message.equalsIgnoreCase(command)) {
                return;
            }
        }

        player.sendMessage("You cannot use that command while in an arena!");
        event.setCancelled(true);
    }

}
