package net.knarcraft.minigames.listener;

import net.knarcraft.knarlib.lib.annotations.NotNull;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaSession;
import net.knarcraft.minigames.arena.dropper.DropperArenaSession;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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
    public void onPlayerDamage(@NotNull EntityDamageEvent event) {
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

        // Only trigger a loss when a player suffers fall damage in a dropper arena (This cannot be cancelled!)
        if (arenaSession instanceof DropperArenaSession && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            arenaSession.triggerLoss();
            return;
        }

        // If set as allowed damage, do nothing, except if the damage is fatal
        if (arenaSession.getArena().getAllowedDamageCauses().contains(event.getCause())) {
            applyFakeDamage(player, event.getDamage(), arenaSession);
            return;
        }

        // If set as trigger loss, trigger a loss
        if (arenaSession.getArena().getLossTriggerDamageCauses().contains(event.getCause())) {
            arenaSession.triggerLoss();
        }
    }

    /**
     * Fakes the damaging of a player
     *
     * @param player       <p>The player to damage</p>
     * @param damage       <p>The raw damage to apply</p>
     * @param arenaSession <p>The arena session to trigger a loss for, if the player reaches 0 damage</p>
     */
    private void applyFakeDamage(@NotNull Player player, double damage, @NotNull ArenaSession arenaSession) {
        double newHealth = player.getHealth() - damage;
        player.sendHurtAnimation(180);
        if (newHealth <= 0) {
            AttributeInstance health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (health != null) {
                player.setHealth(health.getValue());
            }
            arenaSession.triggerLoss();
        } else {
            player.setHealth(newHealth);
            player.setNoDamageTicks(10);
            player.playSound(player, Sound.ENTITY_PLAYER_HURT, 1, 1);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCombustion(@NotNull EntityCombustEvent event) {
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
