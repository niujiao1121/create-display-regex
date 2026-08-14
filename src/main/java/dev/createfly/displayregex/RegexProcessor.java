package dev.createfly.displayregex;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/** Pure Java regex engine, deliberately independent of Minecraft for easy testing. */
public final class RegexProcessor {

    private RegexProcessor() {
    }

    public record Validation(boolean valid, String message) {
        public static Validation ok() {
            return new Validation(true, "");
        }
    }

    public static Validation validate(String pattern, String replacement) {
        if (pattern == null || pattern.isEmpty()) {
            return Validation.ok();
        }
        try {
            Pattern compiled = Pattern.compile(pattern);
            Validation replacementValidation = validateReplacement(compiled, replacement == null ? "" : replacement);
            return replacementValidation.valid() ? Validation.ok() : replacementValidation;
        } catch (PatternSyntaxException e) {
            return new Validation(false, e.getDescription());
        }
    }

    private static Validation validateReplacement(Pattern pattern, String replacement) {
        int groupCount = pattern.matcher("").groupCount();
        for (int i = 0; i < replacement.length(); i++) {
            char c = replacement.charAt(i);
            if (c == '\\') {
                if (++i >= replacement.length()) {
                    return new Validation(false, "Trailing escape in replacement");
                }
                continue;
            }
            if (c != '$') {
                continue;
            }
            if (++i >= replacement.length()) {
                return new Validation(false, "Trailing $ in replacement");
            }
            char next = replacement.charAt(i);
            if (next == '{') {
                int end = replacement.indexOf('}', i + 1);
                if (end < 0 || end == i + 1) {
                    return new Validation(false, "Malformed named group in replacement");
                }
                String groupName = replacement.substring(i + 1, end);
                if (!pattern.namedGroups().containsKey(groupName)) {
                    return new Validation(false, "Replacement references missing named group ${" + groupName + "}");
                }
                i = end;
                continue;
            }
            if (!Character.isDigit(next)) {
                return new Validation(false, "$ must be followed by a group number or {name}");
            }
            int firstGroup = next - '0';
            if (firstGroup > groupCount) {
                return new Validation(false, "Replacement references missing group $" + firstGroup);
            }
            // Java greedily consumes additional digits only while the resulting group number remains valid.
            int group = firstGroup;
            while (i + 1 < replacement.length() && Character.isDigit(replacement.charAt(i + 1))) {
                int candidate = group * 10 + (replacement.charAt(i + 1) - '0');
                if (candidate > groupCount) {
                    break;
                }
                group = candidate;
                i++;
            }
        }
        return Validation.ok();
    }

    /**
     * Applies Java Matcher.replaceAll semantics. Any invalid user rule is fail-open: the original text is returned.
     */
    public static String apply(String input, boolean enabled, String pattern, String replacement) {
        if (!enabled || pattern == null || pattern.isEmpty() || input == null || input.isEmpty()) {
            return input;
        }
        try {
            return Pattern.compile(pattern).matcher(input).replaceAll(replacement == null ? "" : replacement);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            return input;
        }
    }
}
