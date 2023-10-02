# MiniGames

This plugin adds several mini-games.

To create a dropper arena, simply use `/droppercreate <name>`, where \<name> is simply the name used to differentiate
and recognize the arena. Your location will be used as the spawn location for anyone joining the dropper arena. To start
playing, simply use `/dropperjoin <name>`, where \<name> is the same as you specified upon creation.
To modify the arena, use `/dropperedit <name> <property> <value>`.

To create a parkour arena, simply use `/parkourcreate <name>`, where \<name> is simply the name used to differentiate
and recognize the arena. Your location will be used as the spawn location for anyone joining the dropper arena. To start
playing, simply use `/parkourjoin <name>`, where \<name> is the same as you specified upon creation.
To modify the arena, use `/parkouredit <name> <property> <value>`. Use `/parkouredit checkpointAdd here` to add a
checkpoint at your current location.

## Permissions

The only permission normal players will need is `minigames.join` which is set to true by default.

| Node                     | Description                                          |
|--------------------------|------------------------------------------------------|
| minigames.admin          | Gives all permissions.                               |
| minigames.dropper        | Gives all dropper-related permissions.               |
| minigames.parkour        | Gives all parkour-related permissions.               |
| minigames.join           | Allows a player to participate in mini-game arenas.  |
| minigames.join.dropper   | Allows a player to participate in dropper arenas.    |
| minigames.join.parkour   | Allows a player to participate in parkour arenas.    |
| minigames.create         | Allows a player to create a new mini-game arena.     |
| minigames.create.dropper | Allows a player to create a new dropper arena.       |
| minigames.create.parkour | Allows a player to create a new parkour arena.       |
| minigames.edit           | Allows a player to edit an existing mini-game arena. |
| minigames.edit.dropper   | Allows a player to edit an existing dropper arena.   |
| minigames.edit.parkour   | Allows a player to edit an existing parkour arena.   |
| minigames.remove         | Allows a player to remove a mini-game arena.         |
| minigames.remove.dropper | Allows a player to remove a dropper arena.           |
| minigames.remove.parkour | Allows a player to remove a parkour arena.           |

## Commands

| Command                                | Alias    | Arguments                    | Description                                                                         |
|----------------------------------------|----------|------------------------------|-------------------------------------------------------------------------------------|
| /miniGamesReload                       | /mreload |                              | Reloads all data from disk.                                                         |
| /miniGamesLeave                        | /mleave  |                              | Leaves the current mini-game.                                                       |
| /miniGamesMenu                         | /mmenu   |                              | Shows a menu of actions if used while in an arena                                   |
| [/miniGamesReward](#minigamesreward)   | /mreward | [See this](#minigamesreward) | Adds or removes rewards for an arena                                                |
| /dropperList                           | /dlist   |                              | Lists available dropper arenas.                                                     |
| [/dropperJoin](#dropperjoin)           | /djoin   | \<arena> \[mode]             | Joins the selected arena.                                                           |
| /dropperCreate                         | /dcreate | \<name>                      | Creates a new dropper arena with the given name. The spawn is set to your location. |
| /dropperRemove                         | /dremove | \<arena>                     | Removes the specified dropper arena.                                                |
| [/dropperEdit](#dropperedit)           | /dedit   | \<arena> \<option> \[value]  | Gets or sets a dropper arena option.                                                |
| [/dropperGroupSet](#droppergroupset)   | /dgset   | \<arena> \<group>            | Puts the given arena in the given group. Use "none" to remove an existing group.    |
| /dropperGroupList                      | /dglist  | \[group]                     | Lists groups, or the stages of a group if a group is specified.                     |
| [/dropperGroupSwap](#droppergroupswap) | /dgswap  | \<arena1> \<arena2>          | Swaps the two arenas in the group's ordered list.                                   |
| /parkourList                           | /plist   |                              | Lists available parkour arenas.                                                     |
| [/parkourJoin](#parkourjoin)           | /pjoin   | \<arena> \[mode]             | Joins the selected arena.                                                           |
| /parkourCreate                         | /pcreate | \<name>                      | Creates a new parkour arena with the given name. The spawn is set to your location. |
| /parkourRemove                         | /premove | \<arena>                     | Removes the specified parkour arena.                                                |
| [/parkourEdit](#parkouredit)           | /pedit   | \<arena> \<option> \[value]  | Gets or sets a parkour arena option.                                                |
| /parkourGroupSet                       | /pgset   | \<arena> \<group>            | Puts the given arena in the given group. Use "none" to remove an existing group.    |
| /parkourGroupList                      | /pglist  | \[group]                     | Lists groups, or the stages of a group if a group is specified.                     |
| [/parkourGroupSwap](#droppergroupswap) | /pgswap  | \<arena1> \<arena2>          | Swaps the two arenas in the group's ordered list.                                   |

### Command explanation mini-games

#### /miniGamesReward

This command is used to set the rewards for an arena. Rewards can be set for six conditions; a reward for each time the
arena is cleared, a reward for the first time the arena is cleared, a reward for beating your own least deaths record, a
reward for beating your own least time record, a record for beating the global least deaths record and a record for
beating the global least time record. You can give an item, give money, give a permission or execute a console command
with the winning player as an argument.

Note, you can add as many rewards as you want for each reward condition, so you can add a permission and an amount of
currency on the player's first win for example.

`/mreward add dropper|parkour <name> <condition> <type> [value] [value] ...`

`/mreward clear dropper|parkour <name> <condition>`

| Argument  | Type                                                                                                      | Usage                                                       |
|-----------|-----------------------------------------------------------------------------------------------------------|-------------------------------------------------------------|
| action    | add / clear                                                                                               | Whether you are adding a reward or clearing rewards.        |
| type      | dropper / parkour                                                                                         | The type of arena to change rewards for                     |
| name      | _Arena name_                                                                                              | The name of the arena to change rewards for                 |
| condition | WIN / FIRST_WIN / PERSONAL_DEATH_RECORD / PERSONAL_TIME_RECORD / GLOBAL_DEATH_RECORD / GLOBAL_TIME_RECORD | The condition to change rewards for.                        |
| type      | COMMAND / ECONOMY / ITEM / PERMISSION                                                                     | The type of reward to add                                   |
| value     | [See reward types](#reward-types)                                                                         | Input for the reward type. Valid values depend on the type. |

##### Reward types

###### Economy

This reward requires an argument which is a number above zero, which is the amount of currency granted to players.

###### Permission

This reward requires an argument which is the permission string you want to grant the player.

###### Command

The reward requires the command as an argument. Type the full command with spaces and everything, but omit the `/` at
the beginning of the command. Use %player_name% or anything that matches the
RegEx `[<%(\\[{]player[_\\-]?(name)?[>%)\\]}]` (\<player>. \<player-name>, %player_name%, {player}, etc.) as the
placeholder for the rewarded player's name.

###### Item

If used with no arguments, the item in your main hand is used. You can specify a material in the first argument to give
one item of the specified type. You can specify a positive whole number as the second argument to specify the amount of
items to give.

### Command explanation dropper

#### /dropperJoin

This command is used for joining a dropper arena.

`/dropperjoin <arena> [mode]`

| Argument | Usage                                                                                                                |
|----------|----------------------------------------------------------------------------------------------------------------------|
| arena    | The name of the arena to join.                                                                                       |
| mode     | Additional challenge modes can be played after an arena has been cleared once. Available modes: inverted and random. |

#### /dropperEdit

This command allows editing the specified property for the specified dropper arena.

`/dropperedit <arena> <option> [value]`

| Argument | Usage                                 |
|----------|---------------------------------------|
| arena    | The name of the arena to edit.        |
| option   | The option to display or change.      |
| value    | The new value of the selected option. |

These are all the options that can be changed for an arena.

| Option             | Details                                                                                                                                                                     |
|--------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| name               | The name of the arena. Used mainly to select the arena in commands.                                                                                                         |
| spawnLocation      | The spawn location of any player joining the arena. Use `56.546,64.0,44.45` to specify coordinates, or `here`, `this` or any other string to select your current location.  |
| exitLocation       | The location players will be sent to when exiting the arena. If not set, the player will be sent to where they joined from. Valid values are the same as for spawnLocation. |
| verticalVelocity   | The vertical velocity set for players in the arena (basically their falling speed). It must be greater than 0, but max 75. `12.565` and other decimals are allowed.         |
| horizontalVelocity | The horizontal velocity (technically fly speed) set for players in the arena. It must be between 0 and 1, and cannot be 0. Decimals are allowed.                            |
| winBlockType       | The type of block players must hit to win the arena. It can be any material as long as it's a block, and not a type of air.                                                 |

#### /dropperGroupSet

This command is used to set the group of an arena

`/droppergroupset <arena> <group>`

Dropper groups are created and removed as necessary. If you specify a group named "potato", that group is created, and
will be used again if you specify the "potato" group for another arena. You use "none" or "null" to remove an arena from
its group. If the group has no arenas, it will be automatically removed. If the arena already is in a group, it will be
moved to the new group.

#### /dropperGroupSwap

This command is used for changing the order of arenas within a group.

`/droppergroupswap <arena1> <arena2>`

Groups define an order the arenas within that group has to be completed in. Use `/droppergrouplist group` to see the
actual order of the group. So, assuming your arenas in the group looked something like:

1. Forest
2. Sea
3. Nether
4. Savanna

You could use `/droppergroupswap Sea Savanna` to change the order to:

1. Forest
2. Savanna
3. Nether
4. Sea

### Command explanation parkour

#### /parkourJoin

This command is used for joining a dropper arena.

`/parkourjoin <arena> [mode]`

| Argument | Usage                                                                                                     |
|----------|-----------------------------------------------------------------------------------------------------------|
| arena    | The name of the arena to join.                                                                            |
| mode     | Additional challenge modes can be played after an arena has been cleared once. Available modes: hardcore. |

#### /parkourEdit

This command allows editing the specified property for the specified parkour arena.

`/parkouredit <arena> <option> [value]`

| Argument | Usage                                 |
|----------|---------------------------------------|
| arena    | The name of the arena to edit.        |
| option   | The option to display or change.      |
| value    | The new value of the selected option. |

These are all the options that can be changed for an arena.

| Option                    | Details                                                                                                                                                                                       |
|---------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| name                      | The name of the arena. Used mainly to select the arena in commands. Note that underscore (_) cannot be used if you want to utilize placeholders, as it's used to split placeholder arguments. |
| spawnLocation             | The spawn location of any player joining the arena. Use `56.546,64.0,44.45` to specify coordinates, or `here`, `this` or any other string to select your current location.                    |
| exitLocation              | The location players will be sent to when exiting the arena. If not set, the player will be sent to where they joined from. Valid values are the same as for spawnLocation.                   |
| winBlockType              | The type of block players must hit to win the arena. It can be any material as long as it's a block, and not a type of air.                                                                   |
| winLocation               | The location players must reach to win the arena (see spawnLocation for valid values). If set, this overrides, and is used instead of, the win block type.                                    |
| checkpointAdd             | Adds a new checkpoint to the arena's checkpoints (see spawnLocation for valid values).                                                                                                        |
| checkpointClear           | Clears all current checkpoints. Give any value to execute. If not given a value, current checkpoints are shown.                                                                               |
| killPlaneBlocks           | A comma-separated list of materials which will force a loss when stepped on. +WOOL and other [material tags](#notes-about-material-tags) are supported as well.                               |
| obstacleBlocks            | A comma-separated list of materials which will force a loss when touched from any direction. +WOOL and other [material tags](#notes-about-material-tags) are supported as well.               |

## Configuration options

### Shared

| Name                              | Type                | Default                             | Description                                                                                                                                                                                                                       |
|-----------------------------------|---------------------|-------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| liquidHitBoxDepth                 | -1 < decimal < 0    | -0.8                                | This decides how far inside a non-solid block the player must go before detection triggers (-1, 0). The closer to -1 it is, the more accurate it will seem to the player, but the likelihood of not detecting the hit increases.  | 
| solidHitBoxDistance               | 0 < decimal < 1     | 0.2                                 | This decides the distance the player must be from a block below them before a hit triggers (0, 1). If too low, the likelihood of detecting the hit decreases, but it won't look like the player hit the block without being near. |

### Dropper

| Name                              | Type                | Default                             | Description                                                                                                                                                                                                                                           |
|-----------------------------------|---------------------|-------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| blockSneaking                     | true/false          | true                                | Whether to block using the shift key to drop faster than the intended drop speed                                                                                                                                                                      |
| blockSprinting                    | true/false          | true                                | Whether to block using the sprint key for slightly improved air speed                                                                                                                                                                                 |
| verticalVelocity                  | 0 < decimal <= 75   | 1.0                                 | The vertical velocity used as default for all arenas. Must be greater than 0. 3.92 is the max speed of a falling player.                                                                                                                              |
| horizontalVelocity                | 0 < decimal <= 1    | 1.0                                 | The horizontal velocity used as default for all arenas (technically fly-speed). Must be between 0 (exclusive) and 1 (inclusive).                                                                                                                      |
| randomlyInvertedTimer             | 0 < integer <= 3600 | 7                                   | The number of seconds before the randomly inverted game-mode switches between normal and inverted movement (0, 3600]                                                                                                                                  |
| mustDoGroupedInSequence           | true/false          | true                                | Whether grouped dropper arenas must be played in the correct sequence                                                                                                                                                                                 |
| ignoreRecordsUntilGroupBeatenOnce | true/false          | false                               | Whether records won't be registered unless the player has already beaten all arenas in a group. That means players are required to do a second play-through to register a record for a grouped arena.                                                 |
| mustDoNormalModeFirst             | true/false          | true                                | Whether a player must do the normal/default game-mode before playing any other game-modes                                                                                                                                                             |
| liquidHitBoxDepth                 | -1 < decimal < 0    | -0.8                                | This decides how far inside a non-solid block the player must go before detection triggers (-1, 0). The closer to -1 it is, the more accurate it will seem to the player, but the likelihood of not detecting the hit increases.                      | 
| solidHitBoxDistance               | 0 < decimal < 1     | 0.2                                 | This decides the distance the player must be from a block below them before a hit triggers (0, 1). If too low, the likelihood of detecting the hit decreases, but it won't look like the player hit the block without being near.                     |
| blockWhitelist                    | list                | [see this](#blockwhitelist-default) | A whitelist for which blocks won't trigger a loss when hit/passed through. The win block check happens before the loss check, so even blocks on the whitelist can be used as the win-block. "+" denotes a [material tag](#notes-about-material-tags). |

### Parkour

| Name                              | Type       | Default                              | Description                                                                                                                                                                                                                                                |
|-----------------------------------|------------|--------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| enforceCheckpointOrder            | true/false | false                                | Whether to enforce the order in which a player must reach checkpoints. Enabling this ensures that a player cannot trigger a previous checkpoint by accident. It also ensures players cannot skip a checkpoint, even if the arena layout makes it possible. |
| mustDoGroupedInSequence           | true/false | true                                 | Whether grouped dropper arenas must be played in the correct sequence                                                                                                                                                                                      |
| ignoreRecordsUntilGroupBeatenOnce | true/false | false                                | Whether records won't be registered unless the player has already beaten all arenas in a group. That means players are required to do a second play-through to register a record for a grouped arena.                                                      |
| killPlaneBlocks                   | list       | [see this](#killplaneblocks-default) | The types of blocks compromising parkour arenas' kill planes. Add any materials you want to use for the "bottom" of your parkour arenas. +WOOL and other [material tags](#notes-about-material-tags) are supported.                                        |
| obstacleBlocks                    | list       | [see this](#obstacleblocks-default)  | The types of blocks treated as obstacles in every direction. +WOOL and other [material tags](#notes-about-material-tags) are supported.                                                                                                                    |

#### blockWhitelist default:

- WATER
- LAVA
- +WALL_SIGNS
- +STANDING_SIGNS
- STRUCTURE_VOID
- WALL_TORCH
- SOUL_WALL_TORCH
- REDSTONE_WALL_TORCH
- +BANNERS
- +BUTTONS
- +CORALS
- +WALL_CORALS

#### killPlaneBlocks default:

- LAVA
- MAGMA_BLOCK

#### obstacleBlocks default:

- END_ROD
- LIGHTNING_ROD
- CHAIN

## Record placeholders

Player records can be displayed on a leaderboard by using PlaceholderAPI. If you want to display a sign-based
leaderboard, you can use the [Placeholder Signs](https://git.knarcraft.net/EpicKnarvik97/PlaceholderSigns) plugin. The
format for the built-in placeholders is as follows:

`%gameMode_record_recordType_gameModeType_identifierType_identifier_recordPlacing_infoType%`

| Variable       | Values                      | Description                                                                                                                        |
|----------------|-----------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| gameMode       | dropper / parkour           | A selection of which game-mode you are getting a record for                                                                        |
| record         |                             | This must be as-is. It's a selector in case placeholders are added for more than records.                                          |
| recordType     | deaths / time               | Selects the type of record to get (deaths or time).                                                                                |
| gameModeType   | default / inverted / random | Selects the game-mode to get the record for.                                                                                       |
| identifierType | arena / group               | The type of thing the following identifier points to (an arena or an arena group).                                                 |
| identifier     | ?                           | An identifier (the name or UUID) for an arena or a group (whichever was chosen as identifierType).                                 |
| recordPlacing  | 1 / 2 / 3 / ...             | The position of the record to get (1 = first place, 2 = second place, etc.).                                                       |
| infoType       | player / value / combined   | The type of info to get. Player gets the player name, Value gets the value of the achieved record. Combined gets "Player: Record". |

## Notes about material tags

Where a list of material is allowed, this plugin supports using material tags that specify a set of blocks. This makes
it much easier to add a lot of blocks without ending up with hundreds of individual materials. To specify
such a tag, use a `+` character, and then the tag name.
See <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Tag.html">the documentation</a> for a complete overview
of all tags. Only those with type `Tag<Material>` can be used.

Example tags:

- +WOOL
- +WALL_SIGNS
- +ACACIA_LOGS
- +ALL_SIGNS
- +DIAMOND_ORES
- +DIRT
- +DOORS
- +DRAGON_IMMUNE
- +FENCE_GATES
- +FENCES

## Language customization

Most or all strings are customizable. If you place a strings.yml file in the plugin folder, it will take
priority over built-in languages. If you want to change strings, look at MiniGames/src/main/resources/strings.yml for
the proper keys. All strings have the format: ENUM: "Displayed string". The enum must be identical as it defines which
string you have changed. All strings belonging to a language are beneath the language code and indented with two spaces.

The easiest way to add a new language is to copy an existing language and paste it into your custom strings.yml and
change strings as necessary. If you don't include all strings, the remaining will use the built-in English translation.
Remember to change the language code to whichever you use for your custom language.

The interval messages are unique in that if several values are separated by comma (option1,option2,option3), a random
message will be chosen each time it's displayed.

## License

MiniGames is licensed under the GNU Public License Version 3.0. This includes every source and resource file. See the
HEADER file for a more detailed license description.