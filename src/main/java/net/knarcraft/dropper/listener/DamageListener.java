package net.knarcraft.dropper.listener;

import net.knarcraft.dropper.MiniGames;
import net.knarcraft.dropper.arena.dropper.DropperArenaPlayerRegistry;
import net.knarcraft.dropper.arena.dropper.DropperArenaSession;
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
        DropperArenaSession arenaSession = MiniGames.getInstance().getDropperArenaPlayerRegistry().getArenaSession(player.getUniqueId());
        if (arenaSession == null) {
            return;
        }

        event.setCancelled(true);

        // Only trigger a loss when a player suffers fall damage
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            arenaSession.triggerLoss();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCombustion(EntityCombustEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }

        DropperArenaPlayerRegistry registry = MiniGames.getInstance().getDropperArenaPlayerRegistry();
        DropperArenaSession arenaSession = registry.getArenaSession(event.getEntity().getUniqueId());
        if (arenaSession != null) {
            // Cancel combustion for any player in an arena
            event.setCancelled(true);
        }
    }

}
