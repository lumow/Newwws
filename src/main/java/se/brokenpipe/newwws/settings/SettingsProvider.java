package se.brokenpipe.newwws.settings;

import se.brokenpipe.newwws.database.DatabaseSettings;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public interface SettingsProvider {

    DatabaseSettings getDatabaseSettings();

    ResourceSettings getResourceSettings();
}
