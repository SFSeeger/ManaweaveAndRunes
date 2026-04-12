# Manaweave and Runes

A simple magic mod

## Key Resources

### Mana

Mana comes in multiple types.

* **Fire:** Heat, Light, Energy
* **Water:**  Fluid, Life, Healing
* **Air:** Movement, Freedom, Change
* **Earth:** Strength, Growth
* **Entropy:** Chaos, Destruction
* **Order:** Stability, Creation
* **Void:** Emptyness, Absence of Energy
* **Soul:** Living Energy

Mana can be focused using Runes and be stored in Blocks, Items and Entities. It is used to create magical effects such
as spells and rituals

## Blocks, Items Concepts

### Rune Carver (Block)

The _Rune Carving Table_ allows to create Runes of multiple tiers. The tier is dependent on the material the rune is
carved into (Currently only amethyst). To create a rune, a _Rune Carving Template_ as well as a chisel is needed.
Templates can be found around the world and are consumed when used. The template determines the type of the rune, while
the material determines the tier.
Runes can be used in various blocks and items to focus mana and create magical effects

### Mana Collector (Block)

**WARNING: THIS ENTRY IS SUBJECT TO CHANGE SINCE THE REWORK WILL HAPPEN IN 0.2.0**
**THIS SHOULD ALSO BE DATA DRIVEN IN THE FUTURE, BUT FOR NOW IT IS HARDCODED**

Collects Mana of a certain type into a _Storage Rune_ inserted into the block. The Mana output depends on the Rune Tier,
how ideal the collection position is (e.g. Being surrounded by Lava in the Nether produces more)

Plans for Mana Generation:

#### Fire Mana

|           Condition           |        Mana Produced        |
|:-----------------------------:|:---------------------------:|
| "Hot Blocks around Generator" | `Block Value` * Block count |
|          Hot Biomes           |       `Biome Hotness`       |

#### Air Mana

|  Condition   |              Mana Produced               |
|:------------:|:----------------------------------------:|
| Y-Level > 70 | Mana depending on y-level (y-level / 63) |

#### Water Mana

|           Condition           |        Mana Produced        |
|:-----------------------------:|:---------------------------:|
| "Wet Blocks around Generator" | `Block Value` * Block count |
|          Wet Biomes           |       Constant number       |

#### Earth Mana

|    Condition     |               Mana Produced                |
|:----------------:|:------------------------------------------:|
| Surroned by ores | Mana depending on ore type and count (TBD) |

#### Entropy Mana

|               Condition               |  Mana Produced  |
|:-------------------------------------:|:---------------:|
|           Sourounded by TNT           |   Block Count   |
|     Sourrounded by Creepers heads     | 3 * Block Count |
| Sourrounded by Wither Skeleton Skulls | 5 * Block Count |

#### Order Mana

|                            Condition                            |        Mana Produced        |
|:---------------------------------------------------------------:|:---------------------------:|
| Surrounded by "Orderly" blocks (e.g. Bookshelves, Quartz, etc.) | `Block Value` * Block Count |

#### Void Mana

|                        Condition                         |  Mana Produced  |
|:--------------------------------------------------------:|:---------------:|
| Sourounded by Bedrock Blocks (e.g. Coal, Obsidian, etc.) | 3 * Block Count |
|       Is in the end dimension (Ender Dragon fight)       |     20 Mana     |

### Soul Mana

TBD

### Mana Generator

Transforms Items into Mana by using heat to break down the item. Maybe at some sort of catalyst?
Recipe?

### Ritual Anchor

Multiblock structure used to perform rituals. Depending on the tier of the Ritual Anchor, different rituals can be
performed.
Rituals can be used to create large scale magical effects, such as teleporting players, damaging entities, changing the
weather, etc. A Ritual can be started by placing the required items around the ritual anchor and activating it with a
_Mana Weavers Wand_.
After the Anchor consumed all required items, the ritual will consume items it periodically needs (if any) and mana
which can be supplied by connecting it to a _Mana Network_.
It then applies the effect. When a ritual is interrupted, (e.g. by being undersupplied) bad effect can happen,
such as the ritual backfiring and applying the effect to the caster instead of the target.
Rituals can be stopped by using the _Mana Weavers Wand_ on the Ritual Anchor while the ritual is active. This can be
used to stop a ritual without punishment.

#### Marks

Marks are permanent effects that are applied to a player by certain rituals. There are currently 2 sets of marks: Curses
and Boons. Curses have negative effects on the player, while Boons have positive effects.
Marks can be removed by certain rituals at random. Each mark has a ritual associated with it.
Every mark ritual needs a mark container (item) as well as a soul container rune and specific items.
To get all marks currently affecting a player, the player can use a _Scrying Pool_.

##### Available Marks

|        Mark         |                              Effect                               |  Type  |
|:-------------------:|:-----------------------------------------------------------------:|:------:|
| Mark of overheating |              Player takes damage when in hot biomes               | Curse  |
|   Mark of Sinking   | Player falls down way quicker and is unable to resurfice in water | Curse  |
|  Mark of Shrinking  |                Player shrinks and loses some life                 | Curse  |
|   Mark of Growth    |                 Player grows and gains some life                  | Curse? |
|   Mark of Flight    |               Grants the player the ability to fly                |  Boon  |

#### Rituals

|          Ritual           |                                                    Effect                                                    |
|:-------------------------:|:------------------------------------------------------------------------------------------------------------:|
|         Teleport          |               Teleports a Player to the ritual anchor (Becomes better with ritual anchor tier)               |
|      Thunder Ritual       |                                    Creates Lightning at a given Position                                     |
|       Smite Ritual        |                             Creates lightning at the location of an given player                             | 
|       Growth Ritual       |                                  Increases growth speed of crops in an area                                  |
|        Fly Ritual         |                                         Grants fly speed in an area                                          |
|      Shattering Rite      |                                      Breaks blocks around an given area                                      |
|     Sanctuary Ritual      | Creates a dome that protects players by pushing enemies and projectiles out, while granting positive effects |
| Ascended Sanctuary Ritual |                                          Like Sanctuary but better                                           |
|      Particle Ritual      |                       Creates a large amount of particles for a certain amount of time                       |
|   Curse Removal Ritual    |                      Randomly removes one of the curses applied to a player by rituals                       |
|      Marking Rituals      |                                                  See Marks                                                   |

#### Soul Container Rune

Allows to capture a fragment of a players soul and use it for rituals. It determines the target player. A soul must be
stolen sneakily (e.g. without being seen) by right-clicking the player

#### Position Rune

Allows to specify a position for rituals. The position is determined by right-clicking the desired position with the
rune in hand.

### Rune Pedestal

Can be used to display items, supply items to rituals or mana concentrators or supplying mana to the item on top of it.
Using a _Mana Weavers Wand_ on a pedestal connected to a _Mana Network_ allows the player to choose between the item on
top always taking mana first, emptying the item into the network or serving as a storage.

### Mana Concentrator

Can be used to craft new items using mana and some input items. This is also a multiblock which comes in multiple tiers.
Mana can be supplied by connecting it to a _Mana Network_ or by placing _Storage Runes_ inside it. The recipe is
determined by the input items and the tier of the concentrator.

### Scrying Pool
Allows to see the current marks affecting a player. To use it, fill it using an _Echo Shard_ by throwing it on the block or right clicking it with the shard in hand. 
Then right-click the pool with an empty hand to see the marks affecting you. 

## Spell Casting (Mana Weaving?)

### Spell Designer

Allows creating custom Spells by combining Spell Parts in a Rune Circle.
A spell consists of one Spell Type (which can be upgraded / changed with modifiers) and one or more Spell Effects. The
Spell Type determines the way the spell is cast (e.g. Projectile, Area of Effect, etc.) while the Spell Effects
determine what the spell does (e.g. Damage, Heal, etc.).
A Spell Part can be modified by placing the spell part (effect or type) in the middle and modifiers around it. Modifiers
increase spell cooldown and mana cost but make the spell more powerful.
A Spell Can be crafted by placing a Spell Type in the middle and Effects surrounding it.

### Runewrought Workbench

Allows to insert Runes into items or blocks. Used for placing Spells into the _Mana Weavers Wand_ for example.

### Mana Weavers Wand

Allows casting Spells (Should come in different tiers). The Spell is determined by the spell placed into it. Spells can
be switched using the Key `U`.
Consumes mana from the players inventory

### Rune Bracelet

Allows to carry multiple storage runes in one compact item. Can be used anywhere where a storage rune can be used.
Can hold different types of runes.

## Mana Network

A network of blocks that can supply mana to each other. The network is formed by connecting blocks with each other using
a _Mana Connector_.
Select a block by right-clicking it with the connector and then right-clicking another block with the same connector
will connect the two blocks.
Switching away from the connector will stop the connecting process. Sneak + Right Click disconnects the block from the
network.
It does not matter which block is connected to which, only that they form a network.

### Mana Storage

Mana can be stored in a Mana Storage Block or a Storage Rune. The storage Block can store more and multiple types of
mana.

### Mana Transmitter

Allows to connect blocks which are too far away from each other to be connected directly. It does nothing in the network
itself.

## Other Items / Blocks

* Tanzanite / Tanzanite Block (Rare crystal with strong magical properties, used for crafting)
