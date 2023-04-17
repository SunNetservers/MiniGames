package net.knarcraft.minigames;

import net.knarcraft.minigames.arena.ArenaSession;
import net.knarcraft.minigames.arena.dropper.DropperArenaData;
import net.knarcraft.minigames.arena.dropper.DropperArenaGameMode;
import net.knarcraft.minigames.arena.dropper.DropperArenaGroup;
import net.knarcraft.minigames.arena.dropper.DropperArenaHandler;
import net.knarcraft.minigames.arena.dropper.DropperArenaPlayerRegistry;
import net.knarcraft.minigames.arena.dropper.DropperArenaRecordsRegistry;
import net.knarcraft.minigames.arena.dropper.DropperArenaSession;
import net.knarcraft.minigames.arena.parkour.ParkourArenaData;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGameMode;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGroup;
import net.knarcraft.minigames.arena.parkour.ParkourArenaHandler;
import net.knarcraft.minigames.arena.parkour.ParkourArenaPlayerRegistry;
import net.knarcraft.minigames.arena.parkour.ParkourArenaRecordsRegistry;
import net.knarcraft.minigames.arena.record.IntegerRecord;
import net.knarcraft.minigames.arena.record.LongRecord;
import net.knarcraft.minigames.command.LeaveArenaCommand;
import net.knarcraft.minigames.command.ReloadCommand;
import net.knarcraft.minigames.command.dropper.CreateDropperArenaCommand;
import net.knarcraft.minigames.command.dropper.DropperGroupListCommand;
import net.knarcraft.minigames.command.dropper.DropperGroupSetCommand;
import net.knarcraft.minigames.command.dropper.DropperGroupSwapCommand;
import net.knarcraft.minigames.command.dropper.EditDropperArenaCommand;
import net.knarcraft.minigames.command.dropper.EditDropperArenaTabCompleter;
import net.knarcraft.minigames.command.dropper.JoinDropperArenaCommand;
import net.knarcraft.minigames.command.dropper.JoinDropperArenaTabCompleter;
import net.knarcraft.minigames.command.dropper.ListDropperArenaCommand;
import net.knarcraft.minigames.command.dropper.RemoveDropperArenaCommand;
import net.knarcraft.minigames.command.dropper.RemoveDropperArenaTabCompleter;
import net.knarcraft.minigames.command.parkour.CreateParkourArenaCommand;
import net.knarcraft.minigames.command.parkour.EditParkourArenaCommand;
import net.knarcraft.minigames.command.parkour.EditParkourArenaTabCompleter;
import net.knarcraft.minigames.command.parkour.JoinParkourArenaCommand;
import net.knarcraft.minigames.command.parkour.JoinParkourArenaTabCompleter;
import net.knarcraft.minigames.command.parkour.ListParkourArenaCommand;
import net.knarcraft.minigames.command.parkour.ParkourGroupListCommand;
import net.knarcraft.minigames.command.parkour.ParkourGroupSetCommand;
import net.knarcraft.minigames.command.parkour.ParkourGroupSwapCommand;
import net.knarcraft.minigames.command.parkour.RemoveParkourArenaCommand;
import net.knarcraft.minigames.command.parkour.RemoveParkourArenaTabCompleter;
import net.knarcraft.minigames.config.DropperConfiguration;
import net.knarcraft.minigames.config.ParkourConfiguration;
import net.knarcraft.minigames.config.SharedConfiguration;
import net.knarcraft.minigames.container.SerializableMaterial;
import net.knarcraft.minigames.container.SerializableUUID;
import net.knarcraft.minigames.listener.CommandListener;
import net.knarcraft.minigames.listener.DamageListener;
import net.knarcraft.minigames.listener.MoveListener;
import net.knarcraft.minigames.listener.PlayerLeaveListener;
import net.knarcraft.minigames.placeholder.DropperRecordExpansion;
import net.knarcraft.minigames.placeholder.ParkourRecordExpansion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.logging.Level;

/**
 * The dropper plugin's main class
 */
@SuppressWarnings("unused")
public final class MiniGames extends JavaPlugin {

    private static MiniGames instance;
    private SharedConfiguration sharedConfiguration;
    private DropperConfiguration dropperConfiguration;
    private ParkourConfiguration parkourConfiguration;
    private DropperArenaHandler dropperArenaHandler;
    private DropperArenaPlayerRegistry dropperArenaPlayerRegistry;
    private DropperRecordExpansion dropperRecordExpansion;
    private ParkourRecordExpansion parkourRecordExpansion;
    private ParkourArenaHandler parkourArenaHandler;
    private ParkourArenaPlayerRegistry parkourArenaPlayerRegistry;

    /**
     * Gets an instance of this plugin
     *
     * @return <p>An instance of this plugin, or null if not initialized yet.</p>
     */
    public static MiniGames getInstance() {
        return instance;
    }

    /**
     * Gets the dropper arena handler for this instance
     *
     * @return <p>A dropper arena handler</p>
     */
    public DropperArenaHandler getDropperArenaHandler() {
        return this.dropperArenaHandler;
    }

    /**
     * Gets the parkour arena handler for this instance
     *
     * @return <p>A parkour arena handler</p>
     */
    public ParkourArenaHandler getParkourArenaHandler() {
        return this.parkourArenaHandler;
    }

    /**
     * Gets the dropper arena player registry for this instance
     *
     * @return <p>A dropper arena player registry</p>
     */
    public DropperArenaPlayerRegistry getDropperArenaPlayerRegistry() {
        return this.dropperArenaPlayerRegistry;
    }

    /**
     * Gets the parkour arena player registry for this instance
     *
     * @return <p>A parkour arena player registry</p>
     */
    public ParkourArenaPlayerRegistry getParkourArenaPlayerRegistry() {
        return this.parkourArenaPlayerRegistry;
    }

    /**
     * Gets the shared configuration
     *
     * <p>The configuration for options which don't affect specific types of mini-games.</p>
     *
     * @return <p>The shared configuration</p>
     */
    public SharedConfiguration getSharedConfiguration() {
        return this.sharedConfiguration;
    }

    /**
     * Gets the dropper configuration
     *
     * @return <p>The dropper configuration</p>
     */
    public DropperConfiguration getDropperConfiguration() {
        return this.dropperConfiguration;
    }

    /**
     * Gets the parkour configuration
     *
     * @return <p>The parkour configuration</p>
     */
    public ParkourConfiguration getParkourConfiguration() {
        return this.parkourConfiguration;
    }

    /**
     * Gets the current session of the given player
     *
     * @param playerId <p>The id of the player to get a session for</p>
     * @return <p>The player's current session, or null if not found</p>
     */
    public @Nullable ArenaSession getSession(@NotNull UUID playerId) {
        DropperArenaSession dropperArenaSession = dropperArenaPlayerRegistry.getArenaSession(playerId);
        if (dropperArenaSession != null) {
            return dropperArenaSession;
        }

        return parkourArenaPlayerRegistry.getArenaSession(playerId);
    }

    /**
     * Logs a message
     *
     * @param level   <p>The message level to log at</p>
     * @param message <p>The message to log</p>
     */
    public static void log(Level level, String message) {
        MiniGames.getInstance().getLogger().log(level, message);
    }

    /**
     * Reloads all configurations and data from disk
     */
    public void reload() {
        // Load all arenas again
        this.dropperArenaHandler.load();
        this.parkourArenaHandler.load();

        // Reload configuration
        this.reloadConfig();
        this.sharedConfiguration.load(this.getConfig());
        this.dropperConfiguration.load(this.getConfig());
        this.parkourConfiguration.load(this.getConfig());

        // Clear record caches
        this.dropperRecordExpansion.clearCaches();
        this.parkourRecordExpansion.clearCaches();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        // Register serialization classes
        ConfigurationSerialization.registerClass(SerializableMaterial.class);
        ConfigurationSerialization.registerClass(DropperArenaRecordsRegistry.class);
        ConfigurationSerialization.registerClass(SerializableUUID.class);
        ConfigurationSerialization.registerClass(DropperArenaData.class);
        ConfigurationSerialization.registerClass(DropperArenaGroup.class);
        ConfigurationSerialization.registerClass(DropperArenaGameMode.class);
        ConfigurationSerialization.registerClass(LongRecord.class);
        ConfigurationSerialization.registerClass(IntegerRecord.class);
        ConfigurationSerialization.registerClass(ParkourArenaRecordsRegistry.class);
        ConfigurationSerialization.registerClass(ParkourArenaData.class);
        ConfigurationSerialization.registerClass(ParkourArenaGroup.class);
        ConfigurationSerialization.registerClass(ParkourArenaGameMode.class);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        this.saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();
        this.sharedConfiguration = new SharedConfiguration(this.getConfig());
        this.dropperConfiguration = new DropperConfiguration(this.getConfig());
        this.parkourConfiguration = new ParkourConfiguration(this.getConfig());
        this.dropperArenaPlayerRegistry = new DropperArenaPlayerRegistry();
        this.dropperArenaHandler = new DropperArenaHandler(this.dropperArenaPlayerRegistry);
        this.dropperArenaHandler.load();
        this.parkourArenaPlayerRegistry = new ParkourArenaPlayerRegistry();
        this.parkourArenaHandler = new ParkourArenaHandler(this.parkourArenaPlayerRegistry);
        this.parkourArenaHandler.load();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new DamageListener(), this);
        pluginManager.registerEvents(new MoveListener(this.dropperConfiguration, this.parkourConfiguration), this);
        pluginManager.registerEvents(new PlayerLeaveListener(), this);
        pluginManager.registerEvents(new CommandListener(), this);

        registerCommand("miniGamesReload", new ReloadCommand(), null);
        registerCommand("miniGamesLeave", new LeaveArenaCommand(), null);

        registerCommand("dropperCreate", new CreateDropperArenaCommand(), null);
        registerCommand("dropperList", new ListDropperArenaCommand(), null);
        registerCommand("dropperJoin", new JoinDropperArenaCommand(), new JoinDropperArenaTabCompleter());
        registerCommand("dropperEdit", new EditDropperArenaCommand(this.dropperConfiguration), new EditDropperArenaTabCompleter());
        registerCommand("dropperRemove", new RemoveDropperArenaCommand(), new RemoveDropperArenaTabCompleter());
        registerCommand("dropperGroupSet", new DropperGroupSetCommand(), null);
        registerCommand("dropperGroupSwap", new DropperGroupSwapCommand(), null);
        registerCommand("dropperGroupList", new DropperGroupListCommand(), null);

        registerCommand("parkourCreate", new CreateParkourArenaCommand(), null);
        registerCommand("parkourList", new ListParkourArenaCommand(), null);
        registerCommand("parkourJoin", new JoinParkourArenaCommand(), new JoinParkourArenaTabCompleter());
        registerCommand("parkourEdit", new EditParkourArenaCommand(), new EditParkourArenaTabCompleter());
        registerCommand("parkourRemove", new RemoveParkourArenaCommand(), new RemoveParkourArenaTabCompleter());
        registerCommand("parkourGroupSet", new ParkourGroupSetCommand(), null);
        registerCommand("parkourGroupSwap", new ParkourGroupSwapCommand(), null);
        registerCommand("parkourGroupList", new ParkourGroupListCommand(), null);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.dropperRecordExpansion = new DropperRecordExpansion(this);
            if (!this.dropperRecordExpansion.register()) {
                log(Level.WARNING, "Unable to register PlaceholderAPI dropper expansion!");
            }
            this.parkourRecordExpansion = new ParkourRecordExpansion(this);
            if (!this.parkourRecordExpansion.register()) {
                log(Level.WARNING, "Unable to register PlaceholderAPI parkour expansion!");
            }
        }
    }

    @Override
    public void onDisable() {
        // Throw out currently playing players before exiting
        for (Player player : getServer().getOnlinePlayers()) {
            ArenaSession session = getSession(player.getUniqueId());
            if (session != null) {
                session.triggerQuit(true);
            }
        }
    }

    /**
     * Registers a command
     *
     * @param commandName     <p>The name of the command to register (defined in plugin.yml)</p>
     * @param commandExecutor <p>The executor for the command</p>
     * @param tabCompleter    <p>The tab-completer to use, or null</p>
     */
    private void registerCommand(@NotNull String commandName, @NotNull CommandExecutor commandExecutor,
                                 @Nullable TabCompleter tabCompleter) {
        PluginCommand command = this.getCommand(commandName);
        if (command != null) {
            command.setExecutor(commandExecutor);
            if (tabCompleter != null) {
                command.setTabCompleter(tabCompleter);
            }
        } else {
            log(Level.SEVERE, "Unable to register the command " + commandName);
        }
    }

}