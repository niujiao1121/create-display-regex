package dev.createfly.displayregex.mixin;

import com.zurrtum.create.api.behaviour.display.DisplaySource;
import com.zurrtum.create.content.redstone.displayLink.DisplayLinkContext;
import com.zurrtum.create.content.redstone.displayLink.target.DisplayTargetStats;
import dev.createfly.displayregex.RegexTextTransform;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

/**
 * Hooks the central DisplaySource.transferData path, so the feature works for all Display Sources rather than
 * only Package Address. Both normal text targets and Create's flap display path are covered.
 */
@Mixin(DisplaySource.class)
public abstract class DisplaySourceMixin {

    @Redirect(
        method = "transferData",
        at = @At(
            value = "INVOKE",
            target = "Lcom/zurrtum/create/api/behaviour/display/DisplaySource;provideText(Lcom/zurrtum/create/content/redstone/displayLink/DisplayLinkContext;Lcom/zurrtum/create/content/redstone/displayLink/target/DisplayTargetStats;)Ljava/util/List;"
        )
    )
    private List<MutableComponent> createDisplayRegex$processText(
        DisplaySource instance,
        DisplayLinkContext context,
        DisplayTargetStats stats
    ) {
        return RegexTextTransform.apply(instance.provideText(context, stats), context.sourceConfig());
    }

    @Redirect(
        method = "transferData",
        at = @At(
            value = "INVOKE",
            target = "Lcom/zurrtum/create/api/behaviour/display/DisplaySource;provideFlapDisplayText(Lcom/zurrtum/create/content/redstone/displayLink/DisplayLinkContext;Lcom/zurrtum/create/content/redstone/displayLink/target/DisplayTargetStats;)Ljava/util/List;"
        )
    )
    private List<List<MutableComponent>> createDisplayRegex$processFlapText(
        DisplaySource instance,
        DisplayLinkContext context,
        DisplayTargetStats stats
    ) {
        return RegexTextTransform.applyFlap(instance.provideFlapDisplayText(context, stats), context.sourceConfig());
    }
}
