package com.hp.test.framework.staf;

/**
 * Enumerated list of platforms.
 * 
 */
public enum OSType {
    LINUX, MAC, WINDOWS, UNKNOWN;

    public static OSType getValue(final String a) {
        try {
            return valueOf(a.toUpperCase());
        } catch (final IllegalArgumentException e) {
            if (a.toUpperCase().contains("MAC")) {
                return MAC;
            }
            return UNKNOWN;
        }
    }

}
