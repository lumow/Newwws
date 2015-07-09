package se.brokenpipe.newwws.engine;

import se.brokenpipe.newwws.resource.Resource;
import se.brokenpipe.newwws.settings.ResourceSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public class ResourceEngine implements Runnable, Stoppable {

    private final ResourceSettings resourceSettings;
    private final ExecutorService executorService;
    private volatile boolean keepRunning = true;
    private final long sleepTime = 1000L;
    private final Map<String, Long> lastUpdateMap = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(ResourceEngine.class.getName());

    public ResourceEngine(final ResourceSettings resourceSettings, final ExecutorService executorService) {
        this.resourceSettings = resourceSettings;
        this.executorService = executorService;
    }

    @Override
    public void run() {
        while (keepRunning) {
            List<Resource> resources = resourceSettings.getAllResources();
            for (Resource resource : resources) {
                long lastUpdate;
                if (lastUpdateMap.containsKey(resource.getUrl())) {
                    lastUpdate = lastUpdateMap.get(resource.getUrl());
                } else {
                    long currentTime = System.currentTimeMillis();
                    lastUpdateMap.put(resource.getUrl(), currentTime);
                    lastUpdate = 0;
                }
                if (timeToUpdate(resource, lastUpdate)) {
                    GenericResourceJob job = new GenericResourceJob(resource);
                    executorService.submit(job);
                    setUpdateTime(resource, System.currentTimeMillis());
                }
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                LOGGER.warning("Resource engine thread was interrupted: " + e.getMessage());
            }
        }
    }

    private boolean timeToUpdate(final Resource resource, long lastUpdate) {
        long currentTime = System.currentTimeMillis();
        long updateInterval = resource.getTimer();
        return currentTime > lastUpdate + updateInterval;
    }

    private void setUpdateTime(final Resource resource, final long time) {
        lastUpdateMap.put(resource.getUrl(), time);
    }

    @Override
    public void stop() {
        keepRunning = false;
    }
}
