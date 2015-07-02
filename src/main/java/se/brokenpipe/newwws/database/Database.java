package se.brokenpipe.newwws.database;

import se.brokenpipe.newwws.resource.parser.rss.Channel;
import se.brokenpipe.newwws.resource.parser.rss.Item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-06-30
 */
public class Database {

    private final DatabaseSettings settings;
    private Connection connection;

    public Database(final DatabaseSettings settings) {
        this.settings = settings;
    }

    /**
     * Tries to setup the database for the settings provided.
     */
    public void setup() throws DatabaseException {
        DatabaseType dbType = settings.getDatabaseType();
        try {
            Class.forName(dbType.getDatabaseDriver());
        } catch (ClassNotFoundException ex) {
            throw new DatabaseException("Could not find class " + dbType.getDatabaseDriver(), ex);
        }
    }

    public void insertChannel(final Channel channel) {

    }

    public void insertItem(final Item item) {

    }

    private Connection getConnection() throws DatabaseException {
        try {
            connection = DriverManager.getConnection(settings.getDatabaseType().getJdbcPrefix() + settings.getHost(), settings.getUsername(), settings.getPassword());
        } catch (SQLException e) {
            throw new DatabaseException("Could not connect to '" + settings.getDatabaseType().getJdbcPrefix() + settings.getHost() + "'", e);
        }
        return connection;
    }

    private void closeConnection() throws DatabaseException {
        try {
            // TODO: commit/rollback?
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException("Could not close database", e);
        }
    }
}
