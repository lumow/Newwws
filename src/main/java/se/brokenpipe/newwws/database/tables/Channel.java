package se.brokenpipe.newwws.database.tables;

import org.hibernate.annotations.GenericGenerator;
import se.brokenpipe.newwws.resource.parser.rss.RSSTag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-02
 */
@Entity
public class Channel implements RSSTag {

    public static final String IDENTIFIER = "channel";
    public static final String TITLE_IDENTIFIER = "title";
    public static final String LINK_IDENTIFIER = "link";
    public static final String DESCRIPTION_IDENTIFIER = "description";

    private Long id;
    private String title;
    private String link;
    private String description;

    public Channel() {
    }

    @Id
    @Column(name = "channel_id")
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean isSupported(final String tag) {
        return TITLE_IDENTIFIER.equals(tag) ||
                LINK_IDENTIFIER.equals(tag) ||
                DESCRIPTION_IDENTIFIER.equals(tag);
    }

    @Override
    public void addSubTag(final String tag, final String data) {
        switch (tag) {
            case TITLE_IDENTIFIER:
                setTitle(data);
                break;
            case LINK_IDENTIFIER:
                setLink(data);
                break;
            case DESCRIPTION_IDENTIFIER:
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
