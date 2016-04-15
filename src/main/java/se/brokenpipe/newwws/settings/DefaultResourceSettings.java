package se.brokenpipe.newwws.settings;

import se.brokenpipe.newwws.resource.Resource;
import se.brokenpipe.newwws.resource.ResourceType;
import se.brokenpipe.newwws.resource.parser.rss.RssDomParser;
import se.brokenpipe.newwws.resource.parser.smhi.SmhiJsonParser;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public class DefaultResourceSettings implements ResourceSettings {

    private static final long UPDATE_INTERVAL = 1000 * 60 * 15L; // 15 minutes
    private static final long UPDATE_INTERVAL_LONG = 1000 * 60 * 35L; // 35 minutes
    private final List<Resource> resources = new CopyOnWriteArrayList<>();

    public DefaultResourceSettings() {
        resources.add(new Resource("http://www.di.se/rss", new RssDomParser("Dagens Industri"), UPDATE_INTERVAL, ResourceType.ECONOMY));
        resources.add(new Resource("http://www.svd.se/?service=rss", new RssDomParser("Svenska Dagbladet"), UPDATE_INTERVAL, ResourceType.NEWS));
        resources.add(new Resource("https://bubb.la/rss/nyheter", new RssDomParser("Bubb.la"), UPDATE_INTERVAL, ResourceType.NEWS));
        resources.add(new Resource("https://news.ycombinator.com/rss", new RssDomParser("Hacker News"), UPDATE_INTERVAL, ResourceType.TECHNOLOGY));
        resources.add(new Resource("http://www.breakit.se/feed/artiklar", new RssDomParser("Breakit"), UPDATE_INTERVAL, ResourceType.ECONOMY));
        resources.add(new Resource("http://feeds.wired.com/wired/index", new RssDomParser("Wired"), UPDATE_INTERVAL, ResourceType.TECHNOLOGY));
        resources.add(new Resource("http://feber.se/rss", new RssDomParser("Feber"), UPDATE_INTERVAL, ResourceType.TECHNOLOGY));
        resources.add(new Resource("http://www.corren.se/nyheter/rss/", new RssDomParser("Corren"), UPDATE_INTERVAL, ResourceType.NEWS));
        resources.add(new Resource("http://rss.slashdot.org/Slashdot/slashdotMain", new RssDomParser("Slashdot"), UPDATE_INTERVAL_LONG, ResourceType.TECHNOLOGY));
        resources.add(new Resource("https://finance.yahoo.com/rss/topfinstories", new RssDomParser("Yahoo Finance"), UPDATE_INTERVAL, ResourceType.ECONOMY));
        resources.add(new Resource("http://feeds.bbci.co.uk/news/world/rss.xml", new RssDomParser("BBC World News"), UPDATE_INTERVAL, ResourceType.NEWS));
        // resources.add(new Resource("http://restauranghusman.se/", new HusmanParser("Husman"), 1000 * 60 * 60));
        resources.add(new Resource("http://opendata-download-metfcst.smhi.se/api/category/pmp2g/version/2/geotype/point/lon/15.626/lat/58.409/data.json", new SmhiJsonParser(), UPDATE_INTERVAL, ResourceType.WEATHER));
    }

    @Override
    public List<Resource> getAllResources() {
        return resources;
    }

    @Override
    public void addResource(Resource resource) {
        resources.add(resource);
    }

    @Override
    public void removeResource(final String url) {
        resources.removeIf(resource -> resource.getUrl().equals(url));
    }
}
