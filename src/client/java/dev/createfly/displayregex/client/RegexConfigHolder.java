package dev.createfly.displayregex.client;

public interface RegexConfigHolder {
    boolean createDisplayRegex$isEnabled();
    String createDisplayRegex$getPattern();
    String createDisplayRegex$getReplacement();
    void createDisplayRegex$setRule(boolean enabled, String pattern, String replacement);
}
