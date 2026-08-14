package dev.createfly.displayregex;

import net.minecraft.nbt.CompoundTag;

/**
 * Persistent keys are stored inside Create's existing Display Link Source config.
 * No extra block entity data or custom network packet is required.
 */
public record RegexConfig(boolean enabled, String pattern, String replacement) {

    public static final String KEY_ENABLED = "CreateDisplayRegexEnabled";
    public static final String KEY_PATTERN = "CreateDisplayRegexPattern";
    public static final String KEY_REPLACEMENT = "CreateDisplayRegexReplacement";

    public static final RegexConfig DISABLED = new RegexConfig(false, "", "");

    public static RegexConfig from(CompoundTag tag) {
        return new RegexConfig(
            tag.getBooleanOr(KEY_ENABLED, false),
            tag.getStringOr(KEY_PATTERN, ""),
            tag.getStringOr(KEY_REPLACEMENT, "")
        );
    }

    public void writeTo(CompoundTag tag) {
        tag.putBoolean(KEY_ENABLED, enabled);
        tag.putString(KEY_PATTERN, pattern);
        tag.putString(KEY_REPLACEMENT, replacement);
    }

    public boolean active() {
        return enabled && !pattern.isEmpty();
    }
}
