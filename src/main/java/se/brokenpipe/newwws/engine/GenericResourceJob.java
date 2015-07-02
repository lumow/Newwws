package se.brokenpipe.newwws.engine;

import se.brokenpipe.newwws.resource.Resource;
import se.brokenpipe.newwws.resource.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public class GenericResourceJob implements Runnable, Stoppable {

    private final Resource resource;
    private long lastUpdate = 0;
    private volatile boolean keepRunning = true;
    private final long sleepTime = 1000 * 60L; // 1 minute
    private final Logger LOGGER = Logger.getLogger(GenericResourceJob.class.getName());

    public GenericResourceJob(final Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while (keepRunning) {
            long currentTime = System.currentTimeMillis();
            long updateInterval = resource.getTimer();

            LOGGER.info("Checking if it is time to update resource " + resource.getUrl());
            if (currentTime > lastUpdate + updateInterval) {
                LOGGER.info("Updating resource " + resource.getUrl());
                try {
                    InputStream is = getInputStreamFromURL();
                    resource.getParser().parse(is);
                    lastUpdate = currentTime;
                } catch (IOException e) {
                    LOGGER.warning("Couldn't read resource at [" + resource.getUrl() + "] (" + e.getMessage() + ")");
                } catch (ParseException e) {
                    LOGGER.warning("Couldn't parse resource at [" + resource.getUrl() + "] (" + e.getMessage() + ")");
                }
            }

            try {
                LOGGER.info("Sleeping for " + (sleepTime / 1000) + " seconds");
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                keepRunning = false;
            }
        }
    }

    @Override
    public void stop() {
        keepRunning = false;
    }

    private InputStream getInputStreamFromURL() throws IOException {
        return new URL(resource.getUrl()).openStream();
    }
}
