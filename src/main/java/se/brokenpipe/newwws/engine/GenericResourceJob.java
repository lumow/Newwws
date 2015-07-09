package se.brokenpipe.newwws.engine;

import se.brokenpipe.newwws.resource.Resource;
import se.brokenpipe.newwws.resource.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public class GenericResourceJob implements Runnable {

    private final Resource resource;
    private final Logger LOGGER = Logger.getLogger(GenericResourceJob.class.getName());

    public GenericResourceJob(final Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        LOGGER.info("Updating resource " + resource.getUrl());
        try {
            InputStream is = getInputStreamFromURL();
            resource.getParser().parse(is);
        } catch (IOException e) {
            LOGGER.warning("Couldn't read resource at [" + resource.getUrl() + "] (" + e.getMessage() + ")");
        } catch (ParseException e) {
            LOGGER.warning("Couldn't parse resource at [" + resource.getUrl() + "] (" + e.getMessage() + ")");
        }
    }

    private InputStream getInputStreamFromURL() throws IOException {
        URL url = new URL(resource.getUrl());
        URLConnection urlConnection = url.openConnection();
        urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.132 Safari/537.36");
        return urlConnection.getInputStream();
    }
}
