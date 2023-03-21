package net.knarcraft.dropper.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }

        //TODO: Check if the player is in the arena (return if not)
        //TODO: Cancel the event to prevent the player from taking damage or dying
        //TODO: Kick the player from the arena
        //TODO: Teleport the player to the location they entered the arena from, or to the spawn
    }

}
