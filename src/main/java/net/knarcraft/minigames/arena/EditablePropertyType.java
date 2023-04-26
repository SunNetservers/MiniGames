package net.knarcraft.minigames.arena;

/**
 * The type of one editable property
 */
public enum EditablePropertyType {

    /**
     * The property is a location
     */
    LOCATION,

    /**
     * The property is an arena name
     */
    ARENA_NAME,

    /**
     * The property is a horizontal velocity
     */
    HORIZONTAL_VELOCITY,

    /**
     * The property is a vertical velocity (fly speed)
     */
    VERTICAL_VELOCITY,

    /**
     * The property is a material that specifies a block
     */
    BLOCK_TYPE,

    /**
     * The property clears a checkpoint
     */
    CHECKPOINT_CLEAR,

    /**
     * The property is a comma-separated list of materials
     */
    MATERIAL_LIST

}
