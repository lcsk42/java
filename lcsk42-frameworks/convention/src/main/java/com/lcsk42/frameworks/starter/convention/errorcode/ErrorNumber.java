package com.lcsk42.frameworks.starter.convention.errorcode;

/**
 * Represents an error number as a 3-digit value (0-999).
 * This is an immutable record class that validates its input.
 *
 * @param value the integer value of the error number (0-999 inclusive)
 */
public record ErrorNumber(int value) {
    // Cache for frequently used error numbers (0-999)
    private static final ErrorNumber[] CACHE = new ErrorNumber[1000];

    /**
     * Compact constructor that validates the input value.
     * Runs before the canonical constructor.
     *
     * @throws IllegalArgumentException if value is outside 0-999 range
     */
    public ErrorNumber {
        if (value < 0 || value > 999) {
            throw new IllegalArgumentException("Error number must be 3-digit (0-999)");
        }
    }

    /**
     * Static factory method that provides object caching.
     * Returns cached instances for values 0-999 to reduce object creation.
     *
     * @param value the integer value of the error number
     * @return cached ErrorNumber instance if available, or a new instance
     * @throws IllegalArgumentException if value is outside 0-999 range
     */
    public static ErrorNumber of(int value) {
        // Check cache bounds first for performance
        if (value >= 0 && value < CACHE.length) {
            ErrorNumber cached = CACHE[value];
            if (cached == null) {
                // Thread-safe lazy initialization
                synchronized (CACHE) {
                    cached = CACHE[value];
                    if (cached == null) {
                        cached = new ErrorNumber(value);
                        CACHE[value] = cached;
                    }
                }
            }
            return cached;
        }
        // Fallback for out-of-cache-range values (though constructor will reject them)
        return new ErrorNumber(value);
    }

    /**
     * Returns a 3-digit string representation of the error number.
     * Pads with leading zeros if necessary (e.g., 5 becomes "005").
     *
     * @return formatted 3-digit string
     */
    @Override
    public String toString() {
        return String.format("%03d", value);
    }
}