package net.knarcraft.dropper.listener;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArenaSession;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        DropperArenaSession arenaSession = Dropper.getInstance().getPlayerRegistry().getArenaSession(player.getUniqueId());
        if (arenaSession == null) {
            return;
        }

        event.setCancelled(true);

        // Only trigger a loss when a player suffers fall damage
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            arenaSession.triggerLoss();
        }
    }

}
