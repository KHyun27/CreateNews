package mynews;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// singleton 객체 :
@Getter
public class DatabaseConnector {

    private String databaseUrl;
    private String user;
    private String password;

    private Connection connection;
    private Statement statement;
    private static DatabaseConnector instance = null;

    private DatabaseConnector() {
        databaseUrl = "";
        user = "";
        password = "";
        connection = null;
        statement = null;
    }

    public static DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

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
