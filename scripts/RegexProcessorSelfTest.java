import dev.createfly.displayregex.RegexProcessor;

public final class RegexProcessorSelfTest {
    private static void assertEq(String expected, String actual, String name) {
        if (!expected.equals(actual)) {
            throw new AssertionError(name + " expected=[" + expected + "] actual=[" + actual + "]");
        }
    }

    private static void assertTrue(boolean value, String name) {
        if (!value) throw new AssertionError(name);
    }

    public static void main(String[] args) {
        assertEq("BBB/CCC", RegexProcessor.apply("AAA/BBB/CCC", true, "^[^/]+/", ""), "drop first route segment");
        assertEq("CCC", RegexProcessor.apply("AAA/BBB/CCC", true, "^.*/", ""), "keep final route segment");
        assertEq("B-42", RegexProcessor.apply("A-42", true, "^A", "B"), "replacement");
        assertEq("AAA", RegexProcessor.apply("AAA/BBB", true, "^(?<head>[^/]+)/.*$", "${head}"), "named replacement");
        assertEq("AAA/BBB", RegexProcessor.apply("AAA/BBB", false, "^[^/]+/", ""), "disabled is passthrough");
        assertEq("AAA/BBB", RegexProcessor.apply("AAA/BBB", true, "[", ""), "invalid pattern fail-open");
        assertTrue(RegexProcessor.validate("^[^/]+/", "").valid(), "valid rule accepted");
        assertTrue(!RegexProcessor.validate("[", "").valid(), "invalid pattern rejected");
        assertTrue(!RegexProcessor.validate("(a)", "$2").valid(), "invalid replacement group rejected");
        assertTrue(RegexProcessor.validate("(?<segment>a)", "${segment}").valid(), "valid named replacement group accepted");
        assertTrue(!RegexProcessor.validate("(?<segment>a)", "${missing}").valid(), "invalid named replacement group rejected");
        assertTrue(!RegexProcessor.validate("a", "$x").valid(), "invalid dollar syntax rejected");
        assertTrue(!RegexProcessor.validate("a", "\\").valid(), "trailing replacement escape rejected");
        System.out.println("RegexProcessorSelfTest: all tests passed");
    }
}
