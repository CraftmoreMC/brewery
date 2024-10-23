# Creating custom drink types

Brewery allows you to create new drink types by using a simple datapack.
It gives you as much of flexibility as possible given, including full control over values.


## Definitions
To define a drink, you just need to create new json file in `(your datapack)/data/(your_id)/brewery_drinks/`.

See for example [builtin/default definitions](https://github.com/Patbox/brewery/tree/master/src/main/resources/data/brewery/brewery_drinks).

## Data types
There are few custom data formats used. These will be defined as `{/* NAME */}` in value part
in example instructions below.

Used types:
- TEXT - Minecraft's json format (object) or string using https://placeholders.pb4.eu/user/text-format/
- EXPRESSION - Mathematical expression, written as a string, for example `"(quality - 2) / 5"`
- COLOR - Vanilla color as hex `"#RRGGBB"` or text (`"red"`, `"green"`, etc)
- EFFECT - Consumption effect, see format below
- STRING - Text wrapped in queues for example `"some text here"`
- BOOLEAN - `true` or `false`
- ID - Id of some item/etc, for example `"minecraft:apple"`, `"minecraft:resistance"`
- BARREL TYPE - Name of barrels type or `"*"` for every single one.


Currently supported types of barrels: oak, spruce, birch, dark_oak, acacia, mangrove, jungle, warped, crimson

## Format
Main drink format
```json5
{
  // Current version of the format. It's required
  "version": 3,
  // Name of the drink
  "name": {/* TEXT */},
  // Color of the drink, 
  "color": {/* COLOR */},
  // Changes how item looks. Optional
  "visual": {
    // Changes base item
    "item": "minecraft:potion",
    // Additional nbt data that should be attached to item. Optional
    "components": {/* ITEM COMPONENT DATA */},
    // Model identifier used for server resource pack. Optional
    "model": "minecraft:item/dragon_breath"
  },
  // Value of alcohol added to player, has "quality" and "age" parameters 
  "alcoholic_value": {/* EXPRESSION */},
  // List of Ingredients used in cauldron to create it
  "ingredients": [ 
    {
      // List of item ids, as strings
      "items": [{/* STRINGS */}], 
      // Count of items
      "count": {/* NUMBER */}
    }
  ],
  // Required number of distillation runs
  "distillation_runs": {/* NUMBER */},
  // Quality multiplier added used for drink. has "age" parameter.
  "cooking_quality_multiplier": {/* EXPRESSION */},
  // Base quality function. Should have value of "10", if drink doesn't use barrel. Has "age" parameter
  "base_quality_value": {/* EXPRESSION */},
  // List of definitions of how drink behaves in barrels. Optional
  "barrel_definitions": [ 
    {
      // Barrel type or "*" for any barrel
      "type": {/* BARREL TYPE */},
      // Barrel-specific quality  Has "quality" (equal to base quality) and "age" parameters
      "quality_value": {/* EXPRESSION */},
      // Time in seconds required to reveal drink type
      "reveal_time": {/* NUMBER */},
    }
  ],
  // List of effects applied after drinking. Optional
  "effects": [
    {/* EFFECTS */}
  ],
  // List of effects applied after drinking brew in unfinished state. Optional
  "unfinished_brew_effects": [
    {/* EFFECTS */}
  ],
  // Information seen in book of brewery. Optional (if not present it will be hidden)
  "book_information": {
    // Best cooking time, in seconds. Optional
    "best_cooking_time": {/* NUMBER */},
    // Best time of aging in barrel in seconds. Optional
    "best_barrel_age": {/* NUMBER */},
    // List of best barrel types. Optional
    "best_barrel_type": [
      {/* BARREL TYPE */}
    ]
  }
}
```

## Extra expression functions
- `smooth_value_days(<MINIMUM_TIME_FOR_BEST>, <TIME_WITHOUT_CHANGING>, <FALLOUT_TIME>, age)` - <VALUES> are in days, returns between 0 and 1, or -1 after fallout
- `smooth_value_minutes(<MINIMUM_TIME_FOR_BEST>, <TIME_WITHOUT_CHANGING>, <FALLOUT_TIME>, age)` - <VALUES> are in minutes, returns same as above
- `smooth_value_seconds(<MINIMUM_TIME_FOR_BEST>, <TIME_WITHOUT_CHANGING>, <FALLOUT_TIME>, age)` - <VALUES> are in seconds, returns same as above


## Consumption Effects types

All expressions have access to `age` and `quality` variables

## Attribute
Applies (temporary) attributes to player.
More info about functionality here: https://minecraft.wiki/w/Attribute
```json5
{
  "type": "attributes",
  // Time of the effect, in seconds. If lower than 0, it's not applied
  "time": {/* EXPRESSION */},
  "entries":[
    {
      // Just a name for the attribute. Follow the namespace rules
      "id": {/* unique ID */},
      // Flat value that's used for modifying the attribute. Can be negative.
      "amount": {/* NUMBER */},
      // See the wiki.
      "operation": {/* MODIFIER OPERATION */},
      // The type of attribute you will be changing. See the wiki or "/attribute" command.
      "type": {/* ID of attribute */}
    }
  ]
}
```


### Damage
Applies damage to player
```json5
{
  "type": "damage",
  "id": {/* ID of damage type, can be added with datapacks */},
  "value": {/* EXPRESSION */}
}
```

### Delayed
Applies effect specified time later
```json5
{
  "type": "delayed",
  // Effects to apply
  "entries": [{/* EFFECTS */}],
  // Expression for delay in seconds. If lower than 0, it's not applied
  "delay": {/* EXPRESSION */}
}
```

### Execute command
Executes command as player, ignoring the OP level
```json5
{
  "type": "execute_command",
  // Command to execute
  "command": {/* STRING */},
  // Used to check if it should apply. If lower than 0, it's not applied, Optional
  "apply_check": {/* EXPRESSION */}
}
```

### Freeze
Freezes player (like powdered snow)
```json5
{
  "type": "freeze",
  // Sets player on fire for time in seconds. Also has "current" variable
  // If lower than 0, it's not applied
  "time": {/* EXPRESSION */}
}
```

### Potion effect
Applies specified potion status effect to player
```json5
{
  "type": "potion",
  // Effect given to player
  "effect": {/* ID OF STATUS EFFECT */},
  // Time of the effect, in seconds. If lower than 0, it's not applied
  "time": {/* EXPRESSION */},
  // Value of the effect. If lower than 0, it's not applied. Optional (set to 0 by default)
  "value": {/* EXPRESSION */},
  // Makes particle visible. Optional (defaults to false)
  "particles": {/* BOOLEAN */},
  // Shows icon. Optional (defaults to true)
  "show_icon": {/* BOOLEAN */},
  // Locks effect from being cleared by milk. Optional (defaults to true)
  "locked": {/* BOOLEAN */},
}
```

### Random
Applies random effect from the list
```json5
{
  "type": "random",
  // Effects to select from
  "entries": [{/* EFFECTS */}],
  // Used to check if it should apply. If lower than 0, it's not applied, Optional
  "apply_check": {/* EXPRESSION */}
}
```

### Random Teleport
Teleports player randomly, like a chorus fruit
```json5
{
  "type": "random_teleport", 
  // Used to get max distance. If lower than 0, it's not applied
  "distance": {/* EXPRESSION */}
}
```

### Set Alcohol Level
Executes command as player, ignoring the OP level
```json5
{
  "type": "set_alcohol_level",
  // Sets alcohol level of player, additionally has "current" variable.
  // If lower than 0, it's not applied
  "value": {/* EXPRESSION */}
}
```

### Set on Fire
Sets player on fire
```json5
{
  "type": "set_on_fire",
  // Sets player on fire for time in seconds. Also has "current" variable
  // If lower than 0, it's not applied
  "time": {/* EXPRESSION */}
}
```

### Velocity
Applies velocity to player
```json5
{
  "type": "velocity", 
  // Used to get values for (x, y, z) of velocity
  "x": {/* EXPRESSION */},
  "y": {/* EXPRESSION */},
  "z": {/* EXPRESSION */}
}
```


## Disabling builtin brews

To disable builtin/default brews, you just need to add correct `filter.block` definition in your `pack.mcmeta` file.

Example
```json
{
	"pack": {
		"pack_format": 10,
		"description": "Disable builtin Brewery drinks"
	},
	"filter": {
		"block": [
			{
				"namespace": "brewery",
				"path": "(brewery_drinks)[a-z]*"
			}
		]
	}
}
```



