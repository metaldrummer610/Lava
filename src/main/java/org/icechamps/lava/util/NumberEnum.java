package org.icechamps.lava.util;

/**
 * Represents what kind of number we are, since we have to deal with Java's type erasure...
 */
public enum NumberEnum {
    BYTE,
    DOUBLE,
    FLOAT,
    INTEGER,
    LONG,
    SHORT
}
