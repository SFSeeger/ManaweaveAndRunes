package io.github.sfseeger.manaweave_and_runes.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class AddItemGLM extends LootModifier {
    public static final MapCodec<AddItemGLM> CODEC =
            RecordCodecBuilder.mapCodec(instance -> LootModifier.codecStart(instance)
                    .and(ItemStack.CODEC.fieldOf("item").forGetter(AddItemGLM::item))
                    .apply(instance, AddItemGLM::new));

    private final ItemStack item;

    public AddItemGLM(LootItemCondition[] conditionsIn, ItemStack item) {
        super(conditionsIn);
        this.item = item;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }

        generatedLoot.add(this.item);

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    public ItemStack item() {
        return item;
    }
}
