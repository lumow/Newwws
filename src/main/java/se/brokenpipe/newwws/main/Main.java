package se.brokenpipe.newwws.main;

import se.brokenpipe.newwws.database.Database;
import se.brokenpipe.newwws.database.DatabaseException;
import se.brokenpipe.newwws.engine.ResourceEngine;
import se.brokenpipe.newwws.settings.DefaultSettingsProvider;
import se.brokenpipe.newwws.settings.PropertiesSettingsProvider;
import se.brokenpipe.newwws.settings.SettingsProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-06-30
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getClass().getName());

    public static void main(String[] args) {
        boolean useDefaultSettings = false;
        if (args.length == 0) {
            useDefaultSettings = true;
        } else if (args.length != 1) {
            exitWithMessage("Usage: {0} {1}");
        } else {
            if (!checkInput(args[0])) {
                exitWithMessage("is not a file");
            }
        }

        SettingsProvider settingsProvider = null;
        if (useDefaultSettings) {
            settingsProvider = new DefaultSettingsProvider();
        } else {
            try (FileInputStream fis = new FileInputStream(new File(args[0]))) {
                Properties properties = new Properties();
                properties.load(fis);
                settingsProvider = new PropertiesSettingsProvider(properties);
            } catch (IOException e) {
                exitWithMessage("Could not read file");
            }
        }
        Database database = new Database(settingsProvider.getDatabaseSettings());
        try {
            database.setup();
        } catch (DatabaseException e) {
            exitWithMessage("Couldn't set up database");
        }
        ResourceEngine engine = new ResourceEngine(settingsProvider.getResourceSettings(), Executors.newCachedThreadPool());
        Thread engineThread = new Thread(engine);
        engineThread.start();
    }

    /*
     * Checks that the string provided is:
     * a) a real existing file in the file system
     * b) a 'normal' file, i.e. not a directory
     * c) can be read by the application
     */
    private static boolean checkInput(final String fileString) {
        File file = new File(fileString);
        return file.exists() && file.isFile() && file.canRead();
    }

    /*
     *
     */
    private static void exitWithMessage(final String message) {
        LOGGER.severe(message);
        System.exit(0);
    }
}
