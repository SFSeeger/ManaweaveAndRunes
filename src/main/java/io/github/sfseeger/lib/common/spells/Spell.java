package io.github.sfseeger.lib.common.spells;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.manaweave_and_runes.Config;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spell {
    public static final Codec<Spell> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(Spell::getName),
            SpellPart.CODEC.fieldOf("core").forGetter(Spell::getCore),
            SpellPart.CODEC.listOf().fieldOf("spellParts").forGetter(Spell::getSpellParts)
    ).apply(instance, Spell::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Spell> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, Spell::getName,
            SpellPart.STREAM_CODEC, Spell::getCore,
            ByteBufCodecs.collection(ArrayList::new, SpellPart.STREAM_CODEC), Spell::getSpellParts,
            Spell::new
    );
    private SpellPart core;
    private final List<SpellPart> spellParts = new ArrayList<>();
    private String name = "";

    public Spell() {

    }

    public Spell(String name, SpellPart core, List<SpellPart> spellParts) {
        this.name = name;
        this.core = core;
        this.spellParts.addAll(spellParts);
    }

    public Spell(SpellPart core) {
        this.core = core;
    }

    public SpellCastingResult resolveEffects(HitResult hitResult, AbstractSpellCastingContext context) {
        SpellCastingResult result = SpellCastingResult.SUCCESS;
        for (SpellPart part : spellParts) {
            SpellCastingResult result1 = part.resolveEffect(hitResult, context);
            result = result1.compare(result);
        }
        return result;
    }

    public boolean addSpellPart(SpellPart part) {
        if (spellParts.size() >= 4) {
            return false;
        }
        spellParts.add(part);
        return true;
    }

    public Map<Mana, Integer> getManaCost() {
        if (core == null) return Map.of(); // Annoying check for empty data component

        Map<Mana, Integer> manaCost = new HashMap<>(core.getManaCost());
        spellParts.forEach(
                part -> part.getManaCost().forEach((mana, cost) -> manaCost.merge(mana, cost, Integer::sum)));
        return manaCost;
    }

    public int getCooldown() {
        return Math.max(core.getCooldown() + spellParts.stream().mapToInt(SpellPart::getCooldown).sum(), Config.minimumSpellCooldown);
    }

    public boolean isValid() {
        if (core.getSpellNodeType() != SpellNodeType.TYPE) return false;
        if (spellParts.isEmpty()) return false;

        return spellParts.stream().allMatch(SpellPart::isValid) && core.isValid();
    }

    public SpellPart getCore() {
        return core;
    }

    public void setCore(SpellPart core) {
        this.core = core;
    }

    public List<SpellPart> getSpellParts() {
        return spellParts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
