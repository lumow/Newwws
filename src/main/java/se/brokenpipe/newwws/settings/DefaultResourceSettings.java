package se.brokenpipe.newwws.settings;

import se.brokenpipe.newwws.resource.Resource;
import se.brokenpipe.newwws.resource.parser.rss.RssParser;

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
        resources.add(new Resource("http://www.di.se/rss", new RssParser(), updateInterval));
        resources.add(new Resource("http://www.svd.se/?service=rss", new RssParser(), updateInterval));

        return resources;
    }

    @Override
    public void addResource(Resource resource) {

    }

    @Override
    public void removeResource(String url) {

    }
}
