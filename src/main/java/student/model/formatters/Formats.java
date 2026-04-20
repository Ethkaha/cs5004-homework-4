package student.model.formatters;

/** Output formats supported by the app. */
public enum Formats {
    /** JSON output format. */
    JSON,
    /** XML output format. */
    XML,
    /** CSV output format. */
    CSV,
    /** Human-readable pretty print format. */
    PRETTY;

    /**
     * Parses a format name, returning null when unsupported.
     *
     * @param value the format name string to look up
     * @return the matching Formats value, or null if not found
     */
    public static Formats containsValues(String value) {
        for (Formats format : Formats.values()) {
            if (format.toString().equalsIgnoreCase(value)) {
                return format;
            }
        }
        return null;
    }
}
