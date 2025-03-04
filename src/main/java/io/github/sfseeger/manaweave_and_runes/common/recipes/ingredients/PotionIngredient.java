package io.github.sfseeger.manaweave_and_runes.common.recipes.ingredients;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

import static io.github.sfseeger.manaweave_and_runes.core.init.MRIngredientInit.POTION_INGREDIENT;

public class PotionIngredient implements ICustomIngredient {
    public static final MapCodec<PotionIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("potion_type")
                    .xmap(PotionType::valueOf, PotionType::getSerializedName)
                    .forGetter(e -> e.potionType),
            Codec.list(Potion.CODEC).optionalFieldOf("potions", List.of()).forGetter(e -> e.potions)
    ).apply(instance, PotionIngredient::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PotionIngredient> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.map(PotionType::valueOf, PotionType::getSerializedName), (ing) -> ing.potionType,
            Potion.STREAM_CODEC.apply(ByteBufCodecs.list()), (ing) -> ing.potions,
            PotionIngredient::new
    );

    private final PotionType potionType;
    private final List<Holder<Potion>> potions;

    public PotionIngredient(PotionType potionType, List<Holder<Potion>> potions) {
        this.potionType = potionType;
        this.potions = potions;
    }

    @Override
    public boolean test(ItemStack itemStack) {
        if (!itemStack.is(potionType.potionItem) || !(itemStack.getItem() instanceof PotionItem)) return false;
        PotionContents contents = itemStack.get(DataComponents.POTION_CONTENTS);
        if (contents == null) {
            return false;
        }
        return potions.stream().allMatch(e -> contents.potion().isPresent() && contents.potion().get().equals(e));
    }

    @Override
    public Stream<ItemStack> getItems() {
        return potions.stream().map(e -> {
            ItemStack stack = new ItemStack(potionType.potionItem, 1);
            stack.set(DataComponents.POTION_CONTENTS, new PotionContents(e));
            return stack;
        });
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return POTION_INGREDIENT.get();
    }


    public enum PotionType implements StringRepresentable {
        NORMAL(Items.POTION),
        SPLASH(Items.SPLASH_POTION),
        LINGERING(Items.LINGERING_POTION);

        public final Item potionItem;

        PotionType(Item potionItem) {
            this.potionItem = potionItem;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
