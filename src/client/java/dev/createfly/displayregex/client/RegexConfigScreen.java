package dev.createfly.displayregex.client;

import com.zurrtum.create.client.catnip.gui.AbstractSimiScreen;
import com.zurrtum.create.client.catnip.gui.ScreenOpener;
import com.zurrtum.create.client.foundation.gui.AllIcons;
import com.zurrtum.create.client.foundation.gui.widget.IconButton;
import dev.createfly.displayregex.RegexProcessor;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

/** Small Create-styled child screen opened from the Display Link screen. */
public class RegexConfigScreen extends AbstractSimiScreen {

    private static final int PANEL_WIDTH = 286;
    private static final int PANEL_HEIGHT = 184;

    private final RegexConfigHolder holder;

    private boolean enabled;
    private EditBox patternBox;
    private EditBox replacementBox;
    private IconButton enabledButton;
    private IconButton cancelButton;
    private IconButton confirmButton;
    private RegexProcessor.Validation validation = RegexProcessor.Validation.ok();

    public RegexConfigScreen(RegexConfigHolder holder) {
        super(Component.translatable("create_display_regex.gui.title"));
        this.holder = holder;
        this.enabled = holder.createDisplayRegex$isEnabled();
    }

    @Override
    protected void init() {
        setWindowSize(PANEL_WIDTH, PANEL_HEIGHT);
        super.init();
        clearWidgets();

        patternBox = new EditBox(font, guiLeft + 12, guiTop + 47, PANEL_WIDTH - 24, 18, Component.empty());
        patternBox.setMaxLength(256);
        patternBox.setValue(holder.createDisplayRegex$getPattern());
        patternBox.setResponder(value -> validateRule());

        replacementBox = new EditBox(font, guiLeft + 12, guiTop + 83, PANEL_WIDTH - 24, 18, Component.empty());
        replacementBox.setMaxLength(256);
        replacementBox.setValue(holder.createDisplayRegex$getReplacement());
        replacementBox.setResponder(value -> validateRule());

        enabledButton = new IconButton(guiLeft + PANEL_WIDTH - 30, guiTop + 9, enabled ? AllIcons.I_ACTIVE : AllIcons.I_PASSIVE);
        enabledButton.setToolTip(enabled
            ? Component.translatable("create_display_regex.gui.enabled")
            : Component.translatable("create_display_regex.gui.disabled"));
        enabledButton.withCallback(() -> {
            enabled = !enabled;
            enabledButton.setIcon(enabled ? AllIcons.I_ACTIVE : AllIcons.I_PASSIVE);
            enabledButton.setToolTip(enabled
                ? Component.translatable("create_display_regex.gui.enabled")
                : Component.translatable("create_display_regex.gui.disabled"));
        });

        cancelButton = new IconButton(guiLeft + PANEL_WIDTH - 52, guiTop + PANEL_HEIGHT - 27, AllIcons.I_CONFIG_BACK);
        cancelButton.setToolTip(Component.translatable("create_display_regex.gui.cancel"));
        cancelButton.withCallback(ScreenOpener::openPreviousScreen);

        confirmButton = new IconButton(guiLeft + PANEL_WIDTH - 30, guiTop + PANEL_HEIGHT - 27, AllIcons.I_CONFIRM);
        confirmButton.setToolTip(Component.translatable("create_display_regex.gui.apply"));
        confirmButton.withCallback(this::applyAndClose);

        addRenderableWidget(patternBox);
        addRenderableWidget(replacementBox);
        addRenderableWidget(enabledButton);
        addRenderableWidget(cancelButton);
        addRenderableWidget(confirmButton);

        validateRule();
    }

    private void validateRule() {
        if (patternBox == null || replacementBox == null) {
            return;
        }
        validation = RegexProcessor.validate(patternBox.getValue(), replacementBox.getValue());
        if (confirmButton != null) {
            confirmButton.active = validation.valid();
            confirmButton.green = validation.valid();
        }
    }

    private void applyAndClose() {
        validateRule();
        if (!validation.valid()) {
            return;
        }
        holder.createDisplayRegex$setRule(enabled, patternBox.getValue(), replacementBox.getValue());
        ScreenOpener.openPreviousScreen();
    }

    @Override
    public void onClose() {
        // Escape acts as cancel; the Display Link's previous regex rule is left untouched.
        ScreenOpener.openPreviousScreen();
    }

    @Override
    protected void renderWindow(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        // Create uses the same dark translucent menu backdrop; this inner panel keeps assets optional.
        graphics.fill(guiLeft, guiTop, guiLeft + PANEL_WIDTH, guiTop + PANEL_HEIGHT, 0xE6251D1A);
        graphics.fill(guiLeft + 1, guiTop + 1, guiLeft + PANEL_WIDTH - 1, guiTop + PANEL_HEIGHT - 1, 0xE63A2A23);

        graphics.text(font, title, guiLeft + 12, guiTop + 12, 0xFFF2C16D, false);
        graphics.text(
            font,
            Component.translatable(enabled ? "create_display_regex.gui.enabled" : "create_display_regex.gui.disabled"),
            guiLeft + 12,
            guiTop + 27,
            enabled ? 0xFF80C080 : 0xFFB0B0B0,
            false
        );
        graphics.text(font, Component.translatable("create_display_regex.gui.pattern"), guiLeft + 12, guiTop + 37, 0xFFE8E8E8, false);
        graphics.text(font, Component.translatable("create_display_regex.gui.replacement"), guiLeft + 12, guiTop + 73, 0xFFE8E8E8, false);

        Component status = validation.valid()
            ? Component.translatable("create_display_regex.gui.valid")
            : Component.translatable("create_display_regex.gui.invalid", validation.message());
        graphics.text(font, status, guiLeft + 12, guiTop + 112, validation.valid() ? 0xFFA0D0A0 : 0xFFFF8080, false);
        graphics.text(font, Component.translatable("create_display_regex.gui.usage"), guiLeft + 12, guiTop + 128, 0xFFC8C8C8, false);
        graphics.text(font, Component.translatable("create_display_regex.gui.example"), guiLeft + 12, guiTop + 141, 0xFFC8C8C8, false);
        graphics.text(font, Component.translatable("create_display_regex.gui.example_result"), guiLeft + 12, guiTop + 154, 0xFFA0D0A0, false);
    }
}
