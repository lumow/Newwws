package se.brokenpipe.newwws.settings;

import se.brokenpipe.newwws.resource.Resource;

import java.util.List;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public interface ResourceSettings {

    /**
     * Gets all the (current) resources for this configuration.
     * Has to be thread-safe if addResource is to be implemented.
     * @return A list of the current resources.
     */
    List<Resource> getAllResources();

    /**
     * Adds a resource to this configuration.
     * @param resource The resource to add.
     */
    void addResource(Resource resource);

    /**
     * Removes a resource from this configuration.
     * @param url The URL to the resource to delete.
     */
    void removeResource(String url);
}
