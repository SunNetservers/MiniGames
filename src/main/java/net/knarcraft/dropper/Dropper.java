package net.knarcraft.dropper;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class Dropper extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        //TODO: Keep track of whether players are in a dropper arena, and which arena they are in
        //TODO: Make an event listener that kicks players from an arena if they take damage (EntityDamageEvent).
        // Remember to cancel the event so they don't die.
        //TODO: Make a listener for whether someone in an arena is about to hit a block (for cobwebs or similar). Use 
        // another check in the listener to check if a player is hitting water -> do whatever should be done when winning.
        //TODO: Arena settings: Spawn (where players are teleported to), Velocity (the downwards speed added to players.
        // Might need a scheduler to maintain the speed), Stage (a numeric integer. if set, only allow access if the 
        // previous stage has been cleared), A configurable reward of some sort?
        //TODO: Add a command for joining a specific arena. Only teleport if the stage check succeeds (The server can 
        // use something like https://www.spigotmc.org/resources/commandblocks.62720/ for immersion)
        //TODO: Store various information about players' performance, and hook into PlaceholderAPI
        //TODO: Implement optional time trial/least deaths game-mode somehow
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
