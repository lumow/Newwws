package se.brokenpipe.newwws.settings;

import se.brokenpipe.newwws.resource.Resource;
import se.brokenpipe.newwws.resource.parser.rss.RssDomParser;
import se.brokenpipe.newwws.resource.parser.smhi.SmhiJsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public class DefaultResourceSettings implements ResourceSettings {

    private static final long UPDATE_INTERVAL = 1000 * 60 * 15L; // 15 minutes

    @Override
    public List<Resource> getAllResources() {
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource("http://www.di.se/rss", new RssDomParser("Dagens Industri"), UPDATE_INTERVAL));
        resources.add(new Resource("http://www.svd.se/?service=rss", new RssDomParser("Svenska Dagbladet"), UPDATE_INTERVAL));
        resources.add(new Resource("https://bubb.la/rss/nyheter", new RssDomParser("Bubb.la"), UPDATE_INTERVAL));
        resources.add(new Resource("https://news.ycombinator.com/rss", new RssDomParser("Hacker News"), UPDATE_INTERVAL));
        resources.add(new Resource("http://opendata-download-metfcst.smhi.se/api/category/pmp2g/version/2/geotype/point/lon/15.626/lat/58.409/data.json", new SmhiJsonParser(), UPDATE_INTERVAL));

        return resources;
    }

    @Override
    public void addResource(Resource resource) {

    }

    @Override
    public void removeResource(String url) {

    }
}
