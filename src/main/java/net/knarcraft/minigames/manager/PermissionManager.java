package net.knarcraft.minigames.manager;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A manager that performs all Permission tasks
 */
public final class PermissionManager {

    private static Permission permission;

    private PermissionManager() {

    }

    /**
     * Initializes the permission manager
     *
     * @param permission <p>The permission object to use for everything permission-related</p>
     */
    public static void initialize(Permission permission) {
        PermissionManager.permission = permission;
    }

    /**
     * Checks whether the permission manager has been initialized
     *
     * @return <p>True if the permission manager has been initialized</p>
     */
    public static boolean isInitialized() {
        return PermissionManager.permission != null;
    }

    /**
     * Grants a permanent permission to a player
     *
     * @param player         <p>The player to grant the permission to</p>
     * @param permissionNode <p>The permission node to grant to the player</p>
     */
    public static void addPermission(@NotNull Player player, @NotNull String permissionNode, @Nullable World world) {
        if (world != null) {
            permission.playerAdd(world.getName(), player, permissionNode);
        } else {
            permission.playerAdd(player, permissionNode);
        }
    }

    /**
     * Checks whether the given player has the given permission
     *
     * @param player         <p>The player to check</p>
     * @param permissionNode <p>The permission node to check for</p>
     * @param world          <p>The world to check for the permission</p>
     * @return <p>True if the player has the permission</p>
     */
    public static boolean hasPermission(@NotNull Player player, @NotNull String permissionNode, @Nullable String world) {
        return permission.playerHas(world, player, permissionNode);
    }

}
