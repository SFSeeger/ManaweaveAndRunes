package io.github.sfseeger.manaweave_and_runes.common.api.mana;

import com.mojang.datafixers.types.templates.Tag;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.common.util.DataComponentUtil;

public class Mana {

    String name;
    public Mana(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
