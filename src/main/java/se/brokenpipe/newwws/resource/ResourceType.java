package se.brokenpipe.newwws.resource;

public enum ResourceType {
    NEWS("News"),
    TECHNOLOGY("Technology"),
    ECONOMY("Economy"),
    WEATHER("Weather");

    private final String type;

    ResourceType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
