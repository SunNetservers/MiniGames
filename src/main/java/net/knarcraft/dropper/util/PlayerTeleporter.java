package net.knarcraft.dropper.util;

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
     * @param player   <p>The player about to teleport</p>
     * @param location <p>The location the player should be teleported to</p>
     * @param force    <p>Whether to force a player teleport, even in a vehicle or a passenger</p>
     * @return <p>True if the player was successfully teleported</p>
     */
    public static boolean teleportPlayer(Player player, Location location, boolean force) {
        if (!player.getPassengers().isEmpty()) {
            if (force) {
                for (Entity passenger : player.getPassengers()) {
                    passenger.eject();
                    passenger.teleport(location);
                }
            } else {
                player.sendMessage("You cannot be teleported with a passenger!");
                return false;
            }
        }
        if (player.isInsideVehicle()) {
            if (force && player.getVehicle() != null) {
                Entity vehicle = player.getVehicle();
                player.eject();
                vehicle.teleport(location);
            } else {
                player.sendMessage("You cannot be teleported while in a vehicle");
                return false;
            }
        }
        //Stop the player velocity to prevent unevenness between players
        player.setVelocity(new Vector(0, 0, 0));
        player.teleport(location);
        return true;
    }

}
