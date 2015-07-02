package se.brokenpipe.newwws.settings;

import se.brokenpipe.newwws.database.DatabaseSettings;
import se.brokenpipe.newwws.database.DatabaseType;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public class DefaultSettingsProvider implements SettingsProvider {

    @Override
    public DatabaseSettings getDatabaseSettings() {
        DatabaseSettings dbSettings = new DatabaseSettings("~/.newwws/db", 0, "sa", "defaultPassword", DatabaseType.H2);
        return dbSettings;
    }

    @Override
    public ResourceSettings getResourceSettings() {
        return new DefaultResourceSettings();
    }
}
