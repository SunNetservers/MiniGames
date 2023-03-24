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

## Command explanation

### /dropperjoin

This command is used for joining a dropper arena.

`/droppejoin <arena> [mode]`

| Argument | Usage                                                                                                            |
|----------|------------------------------------------------------------------------------------------------------------------|
| arena    | The name of the arena to join                                                                                    |
| mode     | Additional challenge modes can be played after an arena has been cleared once. Available modes: deaths and time. |

### /dropperedit

This command allows editing the specified property for the specified dropper arena

`/dropperedit <arena> <option> [value]`

| Argument | Usage                                |
|----------|--------------------------------------|
| arena    | The name of the arena to edit        |
| option   | The option to display or change      |
| value    | The new value of the selected option |