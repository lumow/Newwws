package se.brokenpipe.newwws.database;

/**
 * Author: Jacob Arnesson
 * Email:  jacob.arnesson@infor.com
 * Date:   2015-07-01
 */
public class DatabaseSettings {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final DatabaseType databaseType;

    public DatabaseSettings(final String host, final int port, final String username, final String password, final DatabaseType databaseType) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.databaseType = databaseType;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }
}
