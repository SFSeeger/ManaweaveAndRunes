package io.github.sfseeger.lib.common.mana;

import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.network.ManaNetworkNode;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

public interface IManaNetworkSubscriber {
    <T extends IManaHandler> T getManaHandler(@Nullable Direction side);

    ManaNetworkNode getManaNetworkNode();

    void setManaNetworkNode(ManaNetworkNode node);

    void markUpdated();

    default void removeNode(ServerLevel level) {
        ManaNetworkNode node = getManaNetworkNode();
        if (node != null) {
            node.onRemoved(level);
        }
    }
}
