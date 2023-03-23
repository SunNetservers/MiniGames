package net.knarcraft.dropper.listener;

import net.knarcraft.dropper.Dropper;
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
        if (!Dropper.getInstance().getPlayerRegistry().isInArena(player)) {
            return;
        }

        event.setCancelled(true);

        //TODO: Kick the player from the arena
        //TODO: Teleport the player to the location they entered the arena from, or to the spawn
        //TODO: Do whatever else should be done for losing players (sending a message?)
    }

}
