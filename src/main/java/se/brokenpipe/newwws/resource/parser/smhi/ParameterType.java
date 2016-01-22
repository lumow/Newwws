package se.brokenpipe.newwws.resource.parser.smhi;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2016-01-22
 */
public enum ParameterType {
    PRESSURE("msl", "Air pressure"),
    TEMPERATURE("t", "Temperature"),
    WIND_DIRECTION("wd", "Wind direction"),
    WIND_SPEED("ws", "Wind speed"),
    HUMUDITY("r", "Humidity"),
    PRECIPITATION_CATEGORY("pcat", "Precipitation category"),
    PRECIPITATION_MEDIAN("pmedian", "Precipitation median");

    private final String key;
    private final String description;

    ParameterType(String name, String description) {
        this.key = name;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public static ParameterType fromString(String name) {
        for (ParameterType parameterType : values()) {
            if (parameterType.getKey().equals(name)) {
                return parameterType;
            }
        }
        return null;
    }
}
