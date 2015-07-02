package se.brokenpipe.newwws.engine;

import se.brokenpipe.newwws.resource.Resource;
import se.brokenpipe.newwws.settings.ResourceSettings;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public class ResourceEngine implements Runnable {

    private final ResourceSettings resourceSettings;
    private final ExecutorService executorService;
    private final Map<String, Stoppable> currentResources = new HashMap<>();

    public ResourceEngine(final ResourceSettings resourceSettings, final ExecutorService executorService) {
        this.resourceSettings = resourceSettings;
        this.executorService = executorService;
    }

    @Override
    public void run() {
        while (true) {
            List<Resource> resources = resourceSettings.getAllResources();
            cleanUpResourceSet(resources);
            for (Resource resource : resources) {
                if (!currentResources.containsKey(resource.getUrl())) {
                    GenericResourceJob job = new GenericResourceJob(resource);
                    currentResources.put(resource.getUrl(), job);
                    executorService.submit(job);
                }
            }
        }
    }

    private void cleanUpResourceSet(List<Resource> allResources) {
        Iterator<String> iterator = currentResources.keySet().iterator();

        while (iterator.hasNext()) {
            String resourceUrl = iterator.next();
            boolean found = false;
            for (Resource resource : allResources) {
                if (resource.getUrl().equals(resourceUrl)) {
                    found = true;
                }
            }
            if (!found) {
                currentResources.get(resourceUrl).stop();
                iterator.remove();
            }
        }
    }
}
