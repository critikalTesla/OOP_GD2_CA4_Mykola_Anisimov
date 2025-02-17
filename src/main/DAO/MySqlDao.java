package main.DAO;

import main.Exceptions.DaoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlDao {

    public Connection getConnection() throws DaoException {
        String driver = "com.mysql.cj.jdbc.Driver"; // Correct driver class for MySQL Connector/J 8.x
        String url = "jdbc:mysql://localhost:3306/oop_ca4_mykola_anisimov";
        String username = "root";
        String password = "";
        Connection connection = null;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new DaoException("Failed to find driver class: " + e.getMessage());
        } catch (SQLException e) {
            throw new DaoException("Connection failed: " + e.getMessage());
        }
        return connection;
    }

    public void freeConnection(Connection connection) throws DaoException {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to free connection: " + e.getMessage());
        }
    }
}