package mynews;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
    private Connection connection;
    private Statement statement;

    public void init(String databaseURL, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(databaseURL, user, password);
        statement = connection.createStatement();
    }

    public void close() throws SQLException {
        if (statement != null) {
            statement.close();
            statement = null;
        }

        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
}
