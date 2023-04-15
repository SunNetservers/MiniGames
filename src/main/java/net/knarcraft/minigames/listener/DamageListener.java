package net.knarcraft.minigames.listener;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaSession;
import net.knarcraft.minigames.arena.dropper.DropperArenaSession;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * A listener for checking if a player takes damage within a dropper arena
 */
public class DamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        // Only player damage matters
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) event.getEntity();

        // We don't care about damage outside arenas
        ArenaSession arenaSession = MiniGames.getInstance().getSession(player.getUniqueId());
        if (arenaSession == null) {
            return;
        }

        event.setCancelled(true);

        // Only trigger a loss when a player suffers fall damage in a dropper arena
        if (arenaSession instanceof DropperArenaSession && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            arenaSession.triggerLoss();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCombustion(EntityCombustEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }

        ArenaSession arenaSession = MiniGames.getInstance().getSession(event.getEntity().getUniqueId());
        if (arenaSession != null) {
            // Cancel combustion for any player in an arena
            event.setCancelled(true);
        }
    }

}
