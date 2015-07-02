package se.brokenpipe.newwws.database;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public enum DatabaseType {
    H2("org.h2.Driver", "jdbc:h2:");

    private final String databaseDriver;
    private final String jdbcPrefix;

    DatabaseType(final String databaseDriver, final String jdbcPrefix) {
        this.databaseDriver = databaseDriver;
        this.jdbcPrefix = jdbcPrefix;
    }

    public String getDatabaseDriver() {
        return databaseDriver;
    }

    public String getJdbcPrefix() {
        return jdbcPrefix;
    }
}
