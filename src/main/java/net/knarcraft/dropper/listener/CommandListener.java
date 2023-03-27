package net.knarcraft.dropper.listener;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArenaSession;
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
        DropperArenaSession existingSession = Dropper.getInstance().getPlayerRegistry().getArenaSession(
                player.getUniqueId());
        if (existingSession == null) {
            return;
        }

        List<String> allowedCommands = new ArrayList<>();
        allowedCommands.add("/dropperleave");
        allowedCommands.add("/dleave");

        String message = event.getMessage();
        if (!message.startsWith("/")) {
            return;
        }

        for (String command : allowedCommands) {
            if (message.equals(command)) {
                return;
            }
        }

        player.sendMessage("You cannot use that command while in an arena!");
        event.setCancelled(true);
    }

}
