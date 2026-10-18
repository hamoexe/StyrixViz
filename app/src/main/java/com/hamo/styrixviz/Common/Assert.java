package com.hamo.styrixviz.Common;

/**
 * Minimal replacement for junit.framework.Assert so the app does not need the JUnit library.
 * Failures throw AssertionError, like JUnit's version did.
 */
public final class Assert {

    private Assert() {
    }

    public static void fail() {
        throw new AssertionError();
    }

    public static void fail(String message) {
        throw new AssertionError(message);
    }

    public static void assertTrue(boolean condition) {
        if (!condition)
            throw new AssertionError();
    }

    public static void assertNotNull(Object object) {
        if (object == null)
            throw new AssertionError("expected not null");
    }

    public static void assertEquals(Object expected, Object actual) {
        if (expected == null ? actual != null : !expected.equals(actual))
            throw new AssertionError("expected:<" + expected + "> but was:<" + actual + ">");
    }

    public static void assertEquals(int expected, int actual) {
        if (expected != actual)
            throw new AssertionError("expected:<" + expected + "> but was:<" + actual + ">");
    }

    public static void assertEquals(long expected, long actual) {
        if (expected != actual)
            throw new AssertionError("expected:<" + expected + "> but was:<" + actual + ">");
    }
}
