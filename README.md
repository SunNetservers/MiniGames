# Dropper

This is a plugin for a dropper mini-game (try to reach the bottom without hitting any obstacles).
To create an arena, simply use `/droppercreate <name>`, where \<name> is simply the name used to differentiate and
recognize the arena. Your location will be used as the spawn location for anyone joining the dropper arena. To start
playing, simply use `/dropperjoin <name>`, where \<name> is the same as you specified upon creation.
To modify

## Permissions

| Node           | Description                                       |
|----------------|---------------------------------------------------|
| dropper.admin  | Gives all permissions                             |
| dropper.join   | Allows a player to participate in dropper arenas  |
| dropper.create | Allows a player to create a new dropper arena     |
| dropper.edit   | Allows a player to edit an existing dropper arena |
| dropper.remove | Allows a player to remove a dropper arena         |

## Commands

| Command                      | Arguments                   | Description                                     |
|------------------------------|-----------------------------|-------------------------------------------------|
| /dropperlist                 |                             | Lists available dropper arenas                  |
| [/dropperjoin](#dropperjoin) | \<arena> \[mode]            | Joins the selected arena                        |
| /dropperleave                |                             | Leaves the current dropper arena                |
| /droppercreate               | \<name>                     | Creates a new dropper arena with the given name |
| /dropperremove               | \<arena>                    | Removes the specified dropper arena             |
| [/dropperedit](#dropperedit) | \<arena> \<option> \[value] | Gets or sets a dropper arena option             |
| /dropperreload               |                             | Reloads all data from disk                      |

## Command explanation

### /dropperjoin

This command is used for joining a dropper arena.

`/droppejoin <arena> [mode]`

| Argument | Usage                                                                                                            |
|----------|------------------------------------------------------------------------------------------------------------------|
| arena    | The name of the arena to join.                                                                                   |
| mode     | Additional challenge modes can be played after an arena has been cleared once. Available modes: deaths and time. |

### /dropperedit

This command allows editing the specified property for the specified dropper arena

`/dropperedit <arena> <option> [value]`

| Argument | Usage                                 |
|----------|---------------------------------------|
| arena    | The name of the arena to edit.        |
| option   | The option to display or change.      |
| value    | The new value of the selected option. |

These are all the options that can be changed for an arena

| Option             | Details                                                                                                                                                                     |
|--------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| name               | The name of the arena. Used mainly to select the arena in commands.                                                                                                         |
| spawnLocation      | The spawn location of any player joining the arena. Use `56.546,64.0,44.45` to specify coordinates, or `here`, `this` or any other string to select your current location.  |
| exitLocation       | The location players will be sent to when exiting the arena. If not set, the player will be sent to where they joined from. Valid values are the same as for spawnLocation. |
| verticalVelocity   | The vertical velocity set for players in the arena (basically their falling speed). It must be greater than 0, but max 100. `12.565` and other decimals are allowed.        |
| horizontalVelocity | The horizontal velocity (technically fly speed) set for players in the arena. It must be between -1 and 1, and cannot be 0. Decimals are allowed.                           |
| winBlockType       | The type of block players must hit to win the arena. It can be any material as long as it's a block, and not a type of air.                                                 |