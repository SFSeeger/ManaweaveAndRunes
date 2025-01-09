package io.github.sfseeger.lib.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class RenderTypes extends RenderType {
    public static final RenderType MANA_THREAD =
            create("manaweave_and_runes:mana_beam", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP,
                   VertexFormat.Mode.QUADS, 256, false, true,
                   RenderType.CompositeState.builder()
                           .setTransparencyState(ADDITIVE_TRANSPARENCY)
                           .setTextureState(BLOCK_SHEET)
                           .setOutputState(MAIN_TARGET)
                           .setLightmapState(RenderStateShard.LIGHTMAP)
                           .setCullState(RenderStateShard.NO_CULL)
                           .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                           .createCompositeState(true));

    public RenderTypes(String name, VertexFormat format,
            VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling,
            boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

}
