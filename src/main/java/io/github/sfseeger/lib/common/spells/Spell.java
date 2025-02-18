package io.github.sfseeger.lib.common.spells;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.lib.common.LibUtils;
import io.github.sfseeger.lib.common.mana.Mana;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Spell {
    public static final Codec<Spell> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(Spell::getName),
            AbstractSpellType.CODEC.fieldOf("spellType").forGetter(Spell::getSpellType),
            AbstractSpellEffect.CODEC.listOf().fieldOf("effects").forGetter(Spell::getEffectAsNodes),
            Codec.pair(AbstractSpellNode.CODEC.fieldOf("root").codec(),
                       AbstractSpellModifier.CODEC.listOf().fieldOf("modifiers").codec())
                    .listOf()
                    .fieldOf("modifiers")
                    .forGetter(Spell::getModifiersAsPairs)
    ).apply(instance, Spell::fromCodec));

    public static final StreamCodec<RegistryFriendlyByteBuf, Spell> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, Spell::getName,
            AbstractSpellNode.STREAM_CODEC, Spell::getSpellTypeAsNodes,
            ByteBufCodecs.collection(ArrayList::new, AbstractSpellNode.STREAM_CODEC, 16), Spell::getEffectAsNodes,
            ByteBufCodecs.map(
                    HashMap::new,
                    AbstractSpellNode.STREAM_CODEC,
                    ByteBufCodecs.collection(ArrayList::new, AbstractSpellNode.STREAM_CODEC, 16)
            ), Spell::getModifiersAsNodes,
            Spell::new
    );

    private AbstractSpellType spellType;
    private List<AbstractSpellEffect> effects = new ArrayList<>();
    private Map<AbstractSpellNode, List<AbstractSpellModifier>> modifiers = new HashMap<>();
    private String name = "Unnamed Spell";

    public Spell(){

    }

    public Spell(AbstractSpellType spellType, List<AbstractSpellEffect> effects,
            Map<AbstractSpellNode, List<AbstractSpellModifier>> modifiers) {
        this.spellType = spellType;
        this.effects = effects;
        this.modifiers = modifiers;
    }

    public Spell(String name, AbstractSpellNode spellType, List<AbstractSpellNode> effects,
                 Map<AbstractSpellNode, List<AbstractSpellNode>> modifiers){
        this((AbstractSpellType) spellType, effects.stream().map(effect -> (AbstractSpellEffect) effect).toList(),
                modifiers.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().map(modifier -> (AbstractSpellModifier) modifier).toList()
                )));
        setName(name);
    }

    public static Spell fromCodec(String name, AbstractSpellNode type, List<AbstractSpellNode> effects,
            List<Pair<AbstractSpellNode, List<AbstractSpellNode>>> modifiers) {
        Map<AbstractSpellNode, List<AbstractSpellModifier>> modifierMap = modifiers.stream().collect(Collectors.toMap(
                Pair::getFirst,
                pair -> pair.getSecond().stream().map(modifier -> (AbstractSpellModifier) modifier).toList()));
        Spell instance =  new Spell((AbstractSpellType) type,
                         effects.stream().map(effect -> (AbstractSpellEffect) effect).toList(), modifierMap);

        instance.setName(name);

        return instance;
    }

    public void setSpellType(AbstractSpellType spellType) {
        this.spellType = spellType;
    }

    public AbstractSpellType getSpellType() {
        return spellType;
    }

    public AbstractSpellNode getSpellTypeAsNodes() {
        return spellType;
    }

    public List<AbstractSpellEffect> getEffects() {
        return effects;
    }

    public List<AbstractSpellNode> getEffectAsNodes() {
        return List.copyOf(effects);
    }

    public Map<AbstractSpellNode, List<AbstractSpellModifier>> getModifiers() {
        return modifiers;
    }

    public Map<AbstractSpellNode, List<AbstractSpellNode>> getModifiersAsNodes() {
        return modifiers.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().map(modifier -> (AbstractSpellNode) modifier).toList()));
    }

    public List<Pair<AbstractSpellNode, List<AbstractSpellNode>>> getModifiersAsPairs() {
        return modifiers.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue().stream()
                        .map(modifier -> (AbstractSpellNode) modifier)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }


    public Map<Mana, Integer> getManaCost() {
        Map<Mana, Integer> cost = new HashMap<>(spellType.getManaCost());
        for (AbstractSpellEffect effect : effects) {
            for (Map.Entry<Mana, Integer> entry : effect.getManaCost().entrySet()) {
                cost.put(entry.getKey(), cost.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }
        for (List<AbstractSpellModifier> modifier : modifiers.values()) {
            Map<Mana, Integer> ringCost = new HashMap<>();
            for (AbstractSpellModifier m : modifier) {
                for (Map.Entry<Mana, Integer> entry : m.getManaCost().entrySet()) {
                    ringCost.put(entry.getKey(), ringCost.getOrDefault(entry.getKey(), 0) + entry.getValue());
                }
            }
            for (Map.Entry<Mana, Integer> entry : ringCost.entrySet()) {
                cost.put(entry.getKey(),
                         cost.getOrDefault(entry.getKey(), 0) + entry.getValue() * getModifierCostScalar(
                                 modifier.size()));
            }
        }
        return cost;
    }

    private Integer getModifierCostScalar(int size) {
        return (int) Math.pow((double) (size / 5) + 1, 2);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCooldown() { //TODO: make this method context aware
        int cooldown = spellType.getCooldown();
        for (AbstractSpellEffect effect : effects) {
            cooldown += effect.getCooldown();
        }
        for (List<AbstractSpellModifier> modifier : modifiers.values()) {
            for (AbstractSpellModifier m : modifier) {
                cooldown += m.getCooldown();
            }
        }
        return cooldown;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        LibUtils.encode(CODEC, this, registries);
        return tag;
    }

    public static Spell deserializeNBT(CompoundTag tag, HolderLookup.Provider registries) {
        return LibUtils.decode(CODEC, tag, registries);
    }

    public boolean isValid() {
        if (spellType == null) {
            return false;
        }
        if (effects.isEmpty()) {
            return false;
        }
        for (Map.Entry<AbstractSpellNode, List<AbstractSpellModifier>> node : modifiers.entrySet()) {
            if (node.getKey() == null || node.getValue().isEmpty()) {
                return false;
            }

            for (AbstractSpellModifier modifier : node.getValue()) {
                if (node.getKey().getPossibleModifiers().stream().noneMatch(mod -> mod.equals(modifier))) {
                    return false;
                }
            }
        }
        return true;
    }
}
