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
 * Date:   2015-07-01
 */
@Entity
public class Item implements RSSTag {

    private Long id;
    public static final String IDENTIFIER = "item";
    private String link;
    public static final String LINK_IDENTIFIER = "link";
    private String author;
    public static final String AUTHOR_IDENTIFIER = "author";
    private String title;
    public static final String TITLE_IDENTIFIER = "title";
    private String description;
    public static final String DESCRIPTION_IDENTIFIER = "description";
    private String pubDate;
    public static final String PUBDATE_IDENTIFIER = "pubDate";

    public Item() {
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 1000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TITLE_IDENTIFIER).append(": ").append(getTitle()).append(" ").
                append(LINK_IDENTIFIER).append(": ").append(getLink()).append(" ").
                append(DESCRIPTION_IDENTIFIER).append(": ").append(getDescription()).append(" ").
                append(AUTHOR_IDENTIFIER).append(": ").append(getAuthor()).append(" ").
                append(PUBDATE_IDENTIFIER).append(": ").append(getPubDate());
        return sb.toString();
    }

    @Override
    public boolean isSupported(String tag) {
        return tag.equals(LINK_IDENTIFIER) ||
                tag.equals(AUTHOR_IDENTIFIER) ||
                tag.equals(TITLE_IDENTIFIER) ||
                tag.equals(DESCRIPTION_IDENTIFIER) ||
                tag.equals(PUBDATE_IDENTIFIER);
    }

    @Override
    public void addSubTag(String tag, String data) {
        switch (tag) {
            case LINK_IDENTIFIER:
                setLink(data);
                break;
            case AUTHOR_IDENTIFIER:
                setAuthor(data);
                break;
            case TITLE_IDENTIFIER:
                setTitle(data);
                break;
            case DESCRIPTION_IDENTIFIER:
                setDescription(data);
                break;
            case PUBDATE_IDENTIFIER:
                setPubDate(data);
                break;
            default:
                // Not supported
                break;
        }
    }
}
