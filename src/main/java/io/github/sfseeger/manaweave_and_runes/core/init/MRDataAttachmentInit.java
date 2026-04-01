package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.rituals.marks.MarkDataAttachment;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MRDataAttachmentInit {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ManaweaveAndRunes.MODID);

    public static final Supplier<AttachmentType<MarkDataAttachment>> MARKS_DATA_ATTACHMENT_TYPE =
            ATTACHMENT_TYPES.register("mark",
                                      () -> AttachmentType.serializable(MarkDataAttachment::new).copyOnDeath().build());
}
