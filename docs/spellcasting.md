# Design for Spell casting. Subject to change

````mermaid
classDiagram

    class SpellNode{
        String descriptionId
        Map~Mana, Integer~ baseManaCost
        int baseCooldown

        +getManaCost() Map~Mana, Integer~
        +getCooldown() int
        +getPossibleModifiers() Set~SpellModifier~
    }
    SpellNode <|-- SpellType
    class SpellType{
        +onCast(...)
        +onCastOnBlock(...)
        +onCastOnEntity(...)
    }
    SpellNode <|-- SpellEffect
    class SpellEffect{
        +resolve(...)
        +resolveBlock(...)
        +resolveEntity(...)
    }
    SpellNode <|-- SpellModifier
    class SpellModifier{
        +onContextGather()
        +onEffectResolvePre()
        +onEffectResolvePost()
    }

    SpellType --  Spell
    SpellEffect -- Spell
    SpellModifier -- Spell
    class Spell{
        +SpellType type
        +SpellEffect[] effects
        +Map~SpellNode, SpellModifier[]~ modifiers
    }
````
