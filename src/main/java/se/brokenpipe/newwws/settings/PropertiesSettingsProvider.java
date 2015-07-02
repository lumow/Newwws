package se.brokenpipe.newwws.settings;

import se.brokenpipe.newwws.database.DatabaseSettings;

import java.util.Properties;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public class PropertiesSettingsProvider implements SettingsProvider {

    private final Properties properties;

    public PropertiesSettingsProvider(final Properties properties) {
        this.properties = properties;
    }

    @Override
    public DatabaseSettings getDatabaseSettings() {
        return null;
    }

    @Override
    public ResourceSettings getResourceSettings() {
        return null;
    }
}
