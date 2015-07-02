package se.brokenpipe.newwws.resource.parser.rss;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-02
 */
public interface RSSTag {

    boolean isSupported(String tag);

    void addSubTag(String tag, String data);
}
