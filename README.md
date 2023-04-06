# Dropper

This is a plugin for a dropper mini-game (try to reach the bottom without hitting any obstacles).
To create an arena, simply use `/droppercreate <name>`, where \<name> is simply the name used to differentiate and
recognize the arena. Your location will be used as the spawn location for anyone joining the dropper arena. To start
playing, simply use `/dropperjoin <name>`, where \<name> is the same as you specified upon creation.
To modify

## Permissions

| Node           | Description                                        |
|----------------|----------------------------------------------------|
| dropper.admin  | Gives all permissions.                             |
| dropper.join   | Allows a player to participate in dropper arenas.  |
| dropper.create | Allows a player to create a new dropper arena.     |
| dropper.edit   | Allows a player to edit an existing dropper arena. |
| dropper.remove | Allows a player to remove a dropper arena.         |

## Commands

| Command                                 | Alias    | Arguments                   | Description                                                                         |
|-----------------------------------------|----------|-----------------------------|-------------------------------------------------------------------------------------|
| /dropperList                            | /dlist   |                             | Lists available dropper arenas.                                                     |
| [/dropperJoin](#/dropperJoin)           | /djoin   | \<arena> \[mode]            | Joins the selected arena.                                                           |
| /dropperLeave                           | /dleave  |                             | Leaves the current dropper arena.                                                   |
| /dropperCreate                          | /dcreate | \<name>                     | Creates a new dropper arena with the given name. The spawn is set to your location. |
| /dropperRemove                          | /dremove | \<arena>                    | Removes the specified dropper arena.                                                |
| [/dropperEdit](#/dropperEdit)           | /dedit   | \<arena> \<option> \[value] | Gets or sets a dropper arena option.                                                |
| /dropperReload                          | /dreload |                             | Reloads all data from disk.                                                         |
| [/dropperGroupSet](#/dropperGroupSet)   | /dgset   | \<arena> \<group>           | Puts the given arena in the given group. Use "none" to remove an existing group.    |
| /dropperGroupList                       | /dglist  | \[group]                    | Lists groups, or the stages of a group if a group is specified.                     |
| [/dropperGroupSwap](#/dropperGroupSwap) | /dgswap  | \<arena1> \<arena2>         | Swaps the two arenas in the group's ordered list.                                   |

## Command explanation

### /dropperJoin

This command is used for joining a dropper arena.

`/dropperjoin <arena> [mode]`

| Argument | Usage                                                                                                                |
|----------|----------------------------------------------------------------------------------------------------------------------|
| arena    | The name of the arena to join.                                                                                       |
| mode     | Additional challenge modes can be played after an arena has been cleared once. Available modes: inverted and random. |

### /dropperEdit

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

### /dropperGroupSet

This command is used to set the group of an arena

`/droppergroupset <arena> <group>`

Dropper groups are created and removed as necessary. If you specify a group named "potato", that group is created, and
will be used again if you specify the "potato" group for another arena. You use "none" or "null" to remove an arena from
its group. If the group has no arenas, it will be automatically removed. If the arena already is in a group, it will be
moved to the new group.

### /dropperGroupSwap

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

## Record display placeholders

Player records can be displayed on a leaderboard by using PlaceholderAPI. The format for the built-in placeholders is as
follows:

`%dropper_record_recordType_gameModeType_identifierType_identifier_recordPlacing_infoType%`

| Variable       | Values                      | Description                                                                                                                        |
|----------------|-----------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| dropper_record |                             | Denotes that it's a placeholder for a dropper record. Must be present as-is.                                                       |
| recordType     | deaths / time               | Selects the type of record to get (deaths or time).                                                                                |
| gameModeType   | default / inverted / random | Selects the game-mode to get the record for.                                                                                       |
| identifierType | arena                       | This specifies that the following string is an arena identifier.                                                                   |
| identifier     | ?                           | An identifier (the name or UUID) for an arena or a group (whichever was chosen as identifierType).                                 |
| recordPlacing  | 1 / 2 / 3 / ...             | The position of the record to get (1 = first place, 2 = second place, etc.).                                                       |
| infoType       | player / value / combined   | The type of info to get. Player gets the player name, Value gets the value of the achieved record. Combined gets "Player: Record". |