package io.github.sfseeger.lib.client.patchouli;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualInput;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.Optional;

public class RitualComponentProcessor implements IComponentProcessor {
    private RitualInput ritualInput;
    private Component ritual_name;

    @Override
    public void setup(Level level, IVariableProvider variables) {
        String ritualId = variables.get("ritual", level.registryAccess()).asString();
        RegistryAccess registryAccess = level.registryAccess();

        Optional<RitualInput> input = registryAccess.registry(ManaweaveAndRunesRegistries.RITUAL_INPUT_REGISTRY_KEY)
                .flatMap(reg -> Optional.ofNullable(reg.get(ResourceLocation.tryParse(ritualId))));

        Optional<Ritual> ritual = registryAccess.registry(ManaweaveAndRunesRegistries.RITUAL_REGISTRY_KEY)
                .flatMap(reg -> Optional.ofNullable(reg.get(ResourceLocation.tryParse(ritualId))));
        if (ritual.isEmpty() || input.isEmpty()) throw new IllegalArgumentException("Cannot find Ritual");

        ritualInput = input.get();
        ritual_name = Component.translatable(ritual.get().getDescriptionId());
    }

    @Override
    public IVariable process(Level level, String key) {
        //Optional<IVariable> startItem = getIngredient("start_item", key, level);
        //if(startItem.isPresent()) return startItem.get();

        if(key.startsWith("start_item")){
            int index = Integer.parseInt(key.substring("start_item".length())) - 1;
            if (index >= ritualInput.getInitialItemCost().size()) {
                int newIndex = index - ritualInput.getInitialItemCost().size();
                if (!ritualInput.getTickItemCost().isEmpty() && newIndex < ritualInput.getTickItemCost().size()) {
                    Ingredient ingredient = ritualInput.getTickItemCost().get(newIndex);
                    ItemStack[] itemStacks = ingredient.getItems();
                    ItemStack stack = itemStacks.length == 0 ? ItemStack.EMPTY : itemStacks[0];

                    return IVariable.wrap(stack.getItemHolder().getRegisteredName(), level.registryAccess());
                }
                return null;
            }
            ;
            Ingredient ingredient = ritualInput.getInitialItemCost().get(index);
            ItemStack[] itemStacks = ingredient.getItems();
            ItemStack stack = itemStacks.length == 0 ? ItemStack.EMPTY : itemStacks[0];

            return IVariable.wrap(stack.getItemHolder().getRegisteredName(), level.registryAccess());
        }

        if (key.startsWith("mana_type")) {
            int index = Integer.parseInt(key.substring("mana_type".length())) - 1;
            if (index >= ritualInput.getManaCost().size()) return null;
            return IVariable.wrap(
                    ((Mana) ritualInput.getManaCost().keySet().toArray()[index]).getRegistryName().toString());
        }
        if (key.startsWith("mana_amount")) {
            int index = Integer.parseInt(key.substring("mana_amount".length())) - 1;
            if (index >= ritualInput.getManaCost().size()) return null;
            Mana mana = (Mana) ritualInput.getManaCost().keySet().toArray()[index];
            return IVariable.wrap(ritualInput.getManaCost().get(mana), level.registryAccess());
        }
        if (key.equals("ritual_name")) return IVariable.from(ritual_name, level.registryAccess());

        return null;
    }

    private Optional<IVariable> getIngredient(String variable, String key, Level level) {
        if (key.startsWith(variable)) {
            int index = Integer.parseInt(key.substring(variable.length())) - 1;
            if (index >= ritualInput.getInitialItemCost().size()) return Optional.empty();
            Ingredient ingredient = ritualInput.getInitialItemCost().get(index);
            ItemStack[] itemStacks = ingredient.getItems();
            ItemStack stack = itemStacks.length == 0 ? ItemStack.EMPTY : itemStacks[0];

            return Optional.of(IVariable.wrap(stack.getItemHolder().getRegisteredName(), level.registryAccess()));
        }
        return Optional.empty();
    }
}
