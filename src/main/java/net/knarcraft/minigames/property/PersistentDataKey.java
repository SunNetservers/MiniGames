package net.knarcraft.minigames.property;

/**
 * An enum for all persistent data keys used by this plugin
 */
public enum PersistentDataKey {

    MENU_ITEM("MiniGamesMenu", 1799804),
    LEAVE_ITEM("MiniGamesAction", 1799871),
    GIVE_UP_ITEM("MiniGamesAction", 1799872),
    ;

    private final String keyName;
    private final int dataValue;

    /**
     * Instantiates a new persistent data key
     *
     * @param keyName   <p>The name of this key</p>
     * @param dataValue <p>The integer data value of this key</p>
     */
    PersistentDataKey(String keyName, int dataValue) {
        this.keyName = keyName;
        this.dataValue = dataValue;
    }

    /**
     * Gets the name of this persistent data key
     *
     * @return <p>The name of this key</p>
     */
    public String getKeyName() {
        return this.keyName;
    }

    /**
     * Gets the integer data value of this persistent data key
     *
     * @return <p>The integer data value</p>
     */
    public int getDataValue() {
        return this.dataValue;
    }

}
