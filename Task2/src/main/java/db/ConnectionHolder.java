package db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
