package net.knarcraft.dropper;

import net.knarcraft.dropper.arena.DropperArenaHandler;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class Dropper extends JavaPlugin {

    private static Dropper instance;
    private DropperArenaHandler arenaHandler;

    /**
     * Gets an instance of this plugin
     *
     * @return <p>An instance of this plugin, or null if not initialized yet.</p>
     */
    public static Dropper getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        this.arenaHandler = new DropperArenaHandler();
        this.arenaHandler.loadArenas();

        //TODO: Keep track of whether players are in a dropper arena, and which arena they are in
        //TODO: Make an event listener that kicks players from an arena if they take damage (EntityDamageEvent).
        // Remember to cancel the event so they don't die.
        //TODO: Make a listener for whether someone in an arena is about to hit a block (for cobwebs or similar). Use 
        // another check in the listener to check if a player is hitting water -> do whatever should be done when winning.

        //TODO: Arena settings: Spawn (where players are teleported to), Velocity (the downwards speed added to players.
        // Might need a scheduler to maintain the speed), Stage (a numeric integer. if set, only allow access if the 
        // previous stage has been cleared), A configurable reward of some sort?, A name (just for easy differentiation),
        // possibly a leave location to make sure pressure plates won't create an infinite loop

        //TODO: Add a command for joining a specific arena. Only teleport if the stage check succeeds (The server can 
        // use something like https://www.spigotmc.org/resources/commandblocks.62720/ for immersion)
        //TODO: Store various information about players' performance, and hook into PlaceholderAPI
        //TODO: Implement optional time trial/least deaths game-mode somehow

        //TODO: Possibly implement an optional queue mode, which only allows one player inside one dropper arena at any 
        // time (to prevent players from pushing each-other)


        //TODO: Register event listeners
        //TODO: Register commands
    }

    @Override
    public void onDisable() {

    }
}
