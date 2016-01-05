package se.brokenpipe.newwws.settings;

import se.brokenpipe.newwws.resource.Resource;
import se.brokenpipe.newwws.resource.parser.rss.RssDomParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public class DefaultResourceSettings implements ResourceSettings {

    private final long updateInterval = 1000 * 60 * 15L; // 15 minutes

    @Override
    public List<Resource> getAllResources() {
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource("http://www.di.se/rss", new RssDomParser("Dagens Industri"), updateInterval));
        resources.add(new Resource("http://www.svd.se/?service=rss", new RssDomParser("Svenska Dagbladet"), updateInterval));
        resources.add(new Resource("https://bubb.la/rss/nyheter", new RssDomParser("Bubb.la"), updateInterval));
        resources.add(new Resource("https://news.ycombinator.com/rss", new RssDomParser("Hacker News"), updateInterval));

        return resources;
    }

    @Override
    public void addResource(Resource resource) {

    }

    @Override
    public void removeResource(String url) {

    }
}
