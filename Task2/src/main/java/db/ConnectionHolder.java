package db;

import java.sql.Connection;

public class ConnectionHolder {

    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public static void setConnection(Connection connection) {
        connectionHolder.set(connection);
    }

    public static Connection getConnection() {
        return connectionHolder.get();
    }
}
