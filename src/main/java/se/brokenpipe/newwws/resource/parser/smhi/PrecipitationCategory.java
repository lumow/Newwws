package se.brokenpipe.newwws.resource.parser.smhi;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2016-01-22
 */
public enum PrecipitationCategory {
    DRY(0, "dry"),
    SNOW(1, "snow"),
    SLEET(2, "sleet"),
    RAIN(3, "rain"),
    DRIZZLE(4, "drizzle"),
    FREEZING_RAIN(5, "freezing rain"),
    FREEZING_DRIZZLE(6, "freezing drizzle");

    private final int type;
    private final String description;

    PrecipitationCategory(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static PrecipitationCategory fromKey(String key) {
        int intKey = Integer.parseInt(key);
        for (PrecipitationCategory category : values()) {
            if (intKey == category.getType()) {
                return category;
            }
        }
        return null;
    }
}
