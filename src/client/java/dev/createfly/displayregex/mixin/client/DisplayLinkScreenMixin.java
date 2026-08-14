package dev.createfly.displayregex.mixin.client;

import com.zurrtum.create.client.catnip.gui.AbstractSimiScreen;
import com.zurrtum.create.client.catnip.gui.ScreenOpener;
import com.zurrtum.create.client.content.redstone.displayLink.DisplayLinkScreen;
import com.zurrtum.create.client.foundation.gui.AllIcons;
import com.zurrtum.create.client.foundation.gui.widget.IconButton;
import com.zurrtum.create.content.redstone.displayLink.DisplayLinkBlockEntity;
import com.zurrtum.create.infrastructure.packet.c2s.DisplayLinkConfigurationPacket;
import dev.createfly.displayregex.RegexConfig;
import dev.createfly.displayregex.client.RegexConfigHolder;
import dev.createfly.displayregex.client.RegexConfigScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Adds one Create-style configuration button to the existing Display Link screen. */
@Mixin(DisplayLinkScreen.class)
public abstract class DisplayLinkScreenMixin extends AbstractSimiScreen implements RegexConfigHolder {

    @Shadow
    private DisplayLinkBlockEntity blockEntity;

    @Unique
    private boolean createDisplayRegex$loaded;
    @Unique
    private boolean createDisplayRegex$enabled;
    @Unique
    private String createDisplayRegex$pattern = "";
    @Unique
    private String createDisplayRegex$replacement = "";

    @Inject(method = "init", at = @At("TAIL"))
    private void createDisplayRegex$addButton(CallbackInfo ci) {
        if (!createDisplayRegex$loaded) {
            RegexConfig config = RegexConfig.from(blockEntity.getSourceConfig());
            createDisplayRegex$enabled = config.enabled();
            createDisplayRegex$pattern = config.pattern();
            createDisplayRegex$replacement = config.replacement();
            createDisplayRegex$loaded = true;
        }

        // The original confirm button is at x + 202 on the 235px DATA_GATHERER texture.
        IconButton regexButton = new IconButton(guiLeft + 177, guiTop + 138, AllIcons.I_CONFIG_OPEN);
        regexButton.setToolTip(Component.translatable("create_display_regex.gui.open"));
        regexButton.withCallback(() -> ScreenOpener.open(
            (DisplayLinkScreen) (Object) this,
            new RegexConfigScreen(this)
        ));
        addRenderableWidget(regexButton);
    }

    @ModifyArg(
        method = "onClose",
        at = @At(
            value = "INVOKE",
            target = "Lcom/zurrtum/create/infrastructure/packet/c2s/DisplayLinkConfigurationPacket;<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/nbt/CompoundTag;I)V"
        ),
        index = 1
    )
    private CompoundTag createDisplayRegex$appendConfig(CompoundTag sourceData) {
        new RegexConfig(
            createDisplayRegex$enabled,
            createDisplayRegex$pattern,
            createDisplayRegex$replacement
        ).writeTo(sourceData);
        return sourceData;
    }

    @Override
    public boolean createDisplayRegex$isEnabled() {
        return createDisplayRegex$enabled;
    }

    @Override
    public String createDisplayRegex$getPattern() {
        return createDisplayRegex$pattern;
    }

    @Override
    public String createDisplayRegex$getReplacement() {
        return createDisplayRegex$replacement;
    }

    @Override
    public void createDisplayRegex$setRule(boolean enabled, String pattern, String replacement) {
        createDisplayRegex$enabled = enabled;
        createDisplayRegex$pattern = pattern == null ? "" : pattern;
        createDisplayRegex$replacement = replacement == null ? "" : replacement;
    }
}
