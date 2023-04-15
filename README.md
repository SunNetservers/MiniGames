# MiniGames

This plugin adds several mini-games.
To create a dropper arena, simply use `/droppercreate <name>`, where \<name> is simply the name used to differentiate
and
recognize the arena. Your location will be used as the spawn location for anyone joining the dropper arena. To start
playing, simply use `/dropperjoin <name>`, where \<name> is the same as you specified upon creation.
To modify the arena, use `/dropperedit <name> <property> <value>`.

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

| Command                                | Alias    | Arguments                   | Description                                                                         |
|----------------------------------------|----------|-----------------------------|-------------------------------------------------------------------------------------|
| /miniGamesReload                       | /mreload |                             | Reloads all data from disk.                                                         |
| /miniGamesLeave                        | /mleave  |                             | Leaves the current mini-game.                                                       |
| /dropperList                           | /dlist   |                             | Lists available dropper arenas.                                                     |
| [/dropperJoin](#dropperjoin)           | /djoin   | \<arena> \[mode]            | Joins the selected arena.                                                           |
| /dropperCreate                         | /dcreate | \<name>                     | Creates a new dropper arena with the given name. The spawn is set to your location. |
| /dropperRemove                         | /dremove | \<arena>                    | Removes the specified dropper arena.                                                |
| [/dropperEdit](#dropperedit)           | /dedit   | \<arena> \<option> \[value] | Gets or sets a dropper arena option.                                                |
| [/dropperGroupSet](#droppergroupset)   | /dgset   | \<arena> \<group>           | Puts the given arena in the given group. Use "none" to remove an existing group.    |
| /dropperGroupList                      | /dglist  | \[group]                    | Lists groups, or the stages of a group if a group is specified.                     |
| [/dropperGroupSwap](#droppergroupswap) | /dgswap  | \<arena1> \<arena2>         | Swaps the two arenas in the group's ordered list.                                   |

### Command explanation

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

## Configuration options

| Name                              | Type                | Default                             | Description                                                                                                                                                                                                                       |
|-----------------------------------|---------------------|-------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| blockSneaking                     | true/false          | true                                | Whether to block using the shift key to drop faster than the intended drop speed                                                                                                                                                  |
| blockSprinting                    | true/false          | true                                | Whether to block using the sprint key for slightly improved air speed                                                                                                                                                             |
| verticalVelocity                  | 0 < decimal <= 75   | 1.0                                 | The vertical velocity used as default for all arenas. Must be greater than 0. 3.92 is the max speed of a falling player.                                                                                                          |
| horizontalVelocity                | 0 < decimal <= 1    | 1.0                                 | The horizontal velocity used as default for all arenas (technically fly-speed). Must be between 0 (exclusive) and 1 (inclusive).                                                                                                  |
| randomlyInvertedTimer             | 0 < integer <= 3600 | 7                                   | The number of seconds before the randomly inverted game-mode switches between normal and inverted movement (0, 3600]                                                                                                              |
| mustDoGroupedInSequence           | true/false          | true                                | Whether grouped dropper arenas must be played in the correct sequence                                                                                                                                                             |
| ignoreRecordsUntilGroupBeatenOnce | true/false          | false                               | Whether records won't be registered unless the player has already beaten all arenas in a group. That means players are required to do a second play-through to register a record for a grouped arena.                             |
| mustDoNormalModeFirst             | true/false          | true                                | Whether a player must do the normal/default game-mode before playing any other game-modes                                                                                                                                         |
| makePlayersInvisible              | true/false          | false                               | Whether players should be made invisible while playing in a dropper arena                                                                                                                                                         |
| disableHitCollision               | true/false          | true                                | Whether players should have their entity hit collision disabled while in an arena. This prevents players from pushing each-other if in the same arena.                                                                            |
| liquidHitBoxDepth                 | -1 < decimal < 0    | -0.8                                | This decides how far inside a non-solid block the player must go before detection triggers (-1, 0). The closer to -1 it is, the more accurate it will seem to the player, but the likelihood of not detecting the hit increases.  | 
| solidHitBoxDistance               | 0 < decimal < 1     | 0.2                                 | This decides the distance the player must be from a block below them before a hit triggers (0, 1). If too low, the likelihood of detecting the hit decreases, but it won't look like the player hit the block without being near. |
| blockWhitelist                    | list                | [see this](#blockwhitelist-default) | A whitelist for which blocks won't trigger a loss when hit/passed through. The win block check happens before the loss check, so even blocks on the whitelist can be used as the win-block. "+" denotes a material tag.           |

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