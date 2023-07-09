package net.knarcraft.minigames.util;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.config.MiniGameMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * A helper class for teleporting players
 */
public final class PlayerTeleporter {

    private PlayerTeleporter() {

    }

    /**
     * Teleports the given player to the given location
     *
     * <p>Forcing teleport should only be used inside an arena, to prevent the player from becoming stuck.</p>
     *
     * @param player      <p>The player about to teleport</p>
     * @param location    <p>The location the player should be teleported to</p>
     * @param force       <p>Whether to force a player teleport, even in a vehicle or a passenger</p>
     * @param immediately <p>Whether to to the teleportation immediately, not using any timers</p>
     * @return <p>True if the player was successfully teleported</p>
     */
    public static boolean teleportPlayer(Player player, Location location, boolean force, boolean immediately) {
        if (!player.getPassengers().isEmpty()) {
            if (force) {
                for (Entity passenger : player.getPassengers()) {
                    passenger.eject();
                    passenger.teleport(location);
                }
            } else {
                MiniGames.getInstance().getStringFormatter().displayErrorMessage(player,
                        MiniGameMessage.ERROR_TELEPORT_WITH_PASSENGER);
                return false;
            }
        }
        if (player.isInsideVehicle()) {
            if (force && player.getVehicle() != null) {
                Entity vehicle = player.getVehicle();
                player.eject();
                vehicle.teleport(location);
            } else {
                MiniGames.getInstance().getStringFormatter().displayErrorMessage(player,
                        MiniGameMessage.ERROR_TELEPORT_IN_VEHICLE);
                return false;
            }
        }
        // Stop the existing player velocity to prevent unevenness between players
        player.setVelocity(new Vector(0, 0, 0));
        player.setInvulnerable(true);
        player.teleport(location);
        player.setVelocity(new Vector(0, 0, 0));
        // When teleporting a player out of the arena, sometimes the move listener is slow to react, giving the player 
        // lethal velocity, and causing damage. That's why the player is given 5 ticks of invulnerability
        if (!immediately) {
            Bukkit.getScheduler().runTaskLater(MiniGames.getInstance(), () -> player.setInvulnerable(false), 5);
        } else {
            player.setInvulnerable(false);
        }
        return true;
    }

}
