package dev.createfly.displayregex;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

/** Converts display components to post-processed literal components while preserving their top-level Style. */
public final class RegexTextTransform {

    private RegexTextTransform() {
    }

    public static List<MutableComponent> apply(List<MutableComponent> input, CompoundTag sourceConfig) {
        RegexConfig config = RegexConfig.from(sourceConfig);
        if (!config.active()) {
            return input;
        }

        List<MutableComponent> output = new ArrayList<>(input.size());
        for (MutableComponent component : input) {
            String transformed = RegexProcessor.apply(
                component.getString(),
                true,
                config.pattern(),
                config.replacement()
            );
            output.add(Component.literal(transformed).withStyle(component.getStyle()));
        }
        return output;
    }

    /**
     * Flap display lines consist of multiple sections. Process each section separately so the number of
     * components still matches Create's precomputed flap layout.
     */
    public static List<List<MutableComponent>> applyFlap(
        List<List<MutableComponent>> input,
        CompoundTag sourceConfig
    ) {
        RegexConfig config = RegexConfig.from(sourceConfig);
        if (!config.active()) {
            return input;
        }

        List<List<MutableComponent>> output = new ArrayList<>(input.size());
        for (List<MutableComponent> line : input) {
            List<MutableComponent> transformedLine = new ArrayList<>(line.size());
            for (MutableComponent component : line) {
                String transformed = RegexProcessor.apply(
                    component.getString(),
                    true,
                    config.pattern(),
                    config.replacement()
                );
                transformedLine.add(Component.literal(transformed).withStyle(component.getStyle()));
            }
            output.add(transformedLine);
        }
        return output;
    }
}
