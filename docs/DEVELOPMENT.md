# Manaweave and Runes 
A simple magic mod

## Key Resources
### Mana
Mana comes in multiple types. Currently planned are:
* **Fire:** Heat, Destruction
* **Water:** 
* **Air:**
* **Earth:**
* **Entropy:**
* **Order:**
* **Void:** Emptyness, Absence of Energy
* **Soul:** Living Energy
Mana can be focused using Runes and be stored in Blocks, Items and Entities. It is used to create magical effects such as spells and rituals


## Key Blocks and Items
### Rune Carver Table (Block)
The _Rune Carver Table_ allowes to create Runes of multiple tiers. The tier is dependent on the material the rune is carved into. 
Different types if mana can also use different types of materials (e.g. Bone for Soul Mana)

### Mana Concentrator (Block)
The _Mana Concentrator_ is a Crafting Table, which allows to use mana in the crafting process. This can be acived by either linking up a Mana Storage or inserting a Storage Rune for the correct Mana type.
The _Mana Concentrator_ can also be upgraded by building a larger structure around it.

### Mana Collector (Block)
Collects Mana of a certan type into a _Storage Rune_ inserted into the block. The Mana output depends on the Rune Tier, how ideal the collection position is (e.g. Being sorrounded by Lava in the Nether produces more) 

Plans for Mana Generation:
#### Fire Mana
| Condition | Mana Produced |
| :---: | :---: |
|"Hot Blocks around Generator"| `Block Value` * Block count |
|Hot Biomes| `Biome Hotness` |
#### Air Mana
| Condition | Mana Produced |
| :---: | :---: |
| Y-Level > 70 | Mana depending on y-level (y-level / 63) |
#### TBD

### Mana Generator
Transforms Items into Mana. Can be modified using custom recipies

### Ritual Core
![Totally not stolen from Arcan](https://static.wikia.nocookie.net/arcane/images/7/70/Viktors-Hexcore.webp/revision/latest?cb=20241127103710)  
Allows the player to craft complex items and perform Rituals in the world (See Ritual Table). The Ritual Core has multiple tiers and can be expended.
A ritual is created by placing the correct runes in a transmutation circle. Larger Rituals also require runes around the ritual core.
To ensure a ritual going correctly, structure runes (and other items: TBD) can be placed around the ritual core.  
Depending on the ritual, the transmutation circle and runes must be adjusted. A sample transmutation circle is shown below.  
![Transmutation circle example](https://static.wikia.nocookie.net/fma/images/d/de/Edcircle22.jpg/revision/latest?cb=20210301094529)

### Rune Pedastal
Used to store Mana by placing a Rune on top of it. Here items can be recharged and mana can be transfered into other blocks.

## Other Items / Blocks
* Crystal Ore / Item / Block
* Storage Rune (Amethyst, Crystal, TBD)
* Soul Anchor Rune (Bone, Amber, TBD)
* ...


## Rituals and Spells
### Spells
Using an Transmutation circle on a crystal as well as a collection of  _Sorage runes_, spells can be cast. The following Spells are currently planned:

| Spell | Effect | Resources | Upgradable |
| :---: | :---: | :---: | :---: |
| Teleport | Teleports a player a certain ammount of blocks in a given direction | Mana: Void, Soul, Order | [X] |
| Fireball | Summons a fireball, that does damage | Mana: Fire | [X] |
| Elemental Spells tbd... |  |  | |
| Chaos Orb | Randomly creates an elemental effect. Simmilar to DnD spell | Mana: Chaos, Elemental | [X] |
| Shield | Grants Immunity against Damage for a certain time | Mana: Void | [] |
| Energy Beam | Creates an large beam of energy | ... | [] |

### Rituals
|       Ritual       |                     Effect                     |                   Resources                    | Core Tier |
|:------------------:|:----------------------------------------------:|:----------------------------------------------:|:---------:|
|      Teleport      |       Teleports a Player to an Position        | Mana: Void, Soul, Order; Items: Position Rune? |           |
|       Damage       |       Damages Target Entities in an Area       |      Mana: Soul, ???; Items: Soul Anchor?      |           |
| Dome of Protection |                                                |                                                |           |
|       Growth       |       Increased Growth speed in an area        |                                                |           |
|     Fly Speed      |            Grants fly speed in area            |                                                |           |
|        Rain        |                  Creates Rain                  |                                                |           |
|      Thunder       |                Creates Thunder                 |                                                |           |
|        Sun         |                 Clears Weather                 |                                                |           |
|     Banishment     | Banishes Player into an "Punishment Dimension" |                                                |           |

## Workflow / Mod Progression
Rune Carving Table => Resonating Crystal (Collects sorrounding Mana) => Mana Concentrator => Generator / Collector => Basic Mana types (Elemental) => Casting => Ritual Core I => Advanced Runes
=> Advanced Collection / Generation => Casting / Ritual Core II

