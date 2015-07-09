package se.brokenpipe.newwws.database.tables;

import se.brokenpipe.newwws.resource.parser.rss.RSSTag;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-02
 */
public class Channel implements RSSTag {

    public static final String IDENTIFIER = "channel";
    private String title;
    public static final String TITLE_IDENTIFIER = "title";
    private String link;
    public static final String LINK_IDENTIFIER = "link";
    private String description;
    public static final String DESCRIPTION_IDENTIFIER = "description";

    public Channel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }

    @Override
    public boolean isSupported(final String tag) {
        return tag.equals(TITLE_IDENTIFIER) ||
                tag.equals(LINK_IDENTIFIER) ||
                tag.equals(DESCRIPTION_IDENTIFIER);
    }

    @Override
    public void addSubTag(final String tag, final String data) {
        switch (tag) {
            case "title":
                setTitle(data);
                break;
            case "link":
                setLink(data);
                break;
            case "description":
                setDescription(data);
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TITLE_IDENTIFIER).append(": ").append(getTitle()).append(" ").
                append(LINK_IDENTIFIER).append(": ").append(getLink()).append(" ").
                append(DESCRIPTION_IDENTIFIER).append(": ").append(getDescription());
        return sb.toString();
    }
}
