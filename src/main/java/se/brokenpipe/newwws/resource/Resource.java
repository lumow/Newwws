package se.brokenpipe.newwws.resource;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public class Resource {

    private final String url;
    private final ResourceParser parser;
    private final long timer;
    private final ResourceType type;

    public Resource(String url, ResourceParser parser, long timer, ResourceType type) {
        this.url = url;
        this.parser = parser;
        this.timer = timer;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public ResourceParser getParser() {
        return parser;
    }

    public long getTimer() {
        return timer;
    }

    public ResourceType getType() {
        return type;
    }
}
