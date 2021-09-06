package it.polimi.ingsw.server.Model.Enums;

/**
 * Enumerates the levels that a card can have
 */

public enum LevelEnum {
    NONE(0),
    ONE(1),
    TWO(2),
    THREE(3);

    private final int level;

    LevelEnum(int level) {
        this.level = level;
    }

    /**
     * Compares two levels
     *
     * @param l1 first level to be compared
     * @param l2 second level to be compared
     * @return a positive value if l1 is greater than l2,
     * 0 if they're the same level, a negative value if l2 is lower than l2
     */
    public static int compare(LevelEnum l1, LevelEnum l2) {
        return Integer.compare(l1.level, l2.level);
    }

    /**
     * Returns the highest level that's lower than the level passed
     *
     * @param level level of which we want to get the previous
     * @return the previous level of the one passed as a parameter
     * @throws IllegalStateException
     */
    public static LevelEnum previousLevel(LevelEnum level) throws IllegalStateException {
        switch (level) {
            default: {
                throw new IllegalStateException("Unexpected value: " + level);
            }
            case NONE:
            case ONE: {
                return NONE;
            }
            case TWO: {
                return ONE;
            }
            case THREE: {
                return TWO;
            }
        }
    }
}
