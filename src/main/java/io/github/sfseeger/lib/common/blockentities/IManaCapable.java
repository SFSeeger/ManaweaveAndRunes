package io.github.sfseeger.lib.common.blockentities;

import io.github.sfseeger.lib.mana.capability.IManaHandler;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public interface IManaCapable {
    IManaHandler getManaHandler(@Nullable Direction side);
}
