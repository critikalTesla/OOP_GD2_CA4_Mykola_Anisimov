package main.BussinessObject;

import main.DAO.MySqlDao;
import main.Exceptions.DaoException;

import java.sql.*;
import java.util.Scanner;

public class FinanceTracker {

    private MySqlDao dao;

    public FinanceTracker() {
        dao = new MySqlDao();
    }

    public static void main(String[] args) {
        FinanceTracker tracker = new FinanceTracker();
        tracker.listAllExpenses(); // Call the method to list all expenses and calculate total spend
    }

    // Method to list all expenses and calculate total spend
    public void listAllExpenses() {
        Connection connection = null;
        try {
            // Get a connection from the MySqlDao class
            connection = dao.getConnection();

            // SQL query to select all expenses
            String query = "SELECT * FROM expense";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                double totalSpend = 0;
                System.out.println("\nExpenses:");
                while (rs.next()) {
                    int expenseID = rs.getInt("expenseID");
                    String title = rs.getString("title");
                    String category = rs.getString("category");
                    double amount = rs.getDouble("amount");
                    Date dateIncurred = rs.getDate("dateIncurred");

                    // Print expense details
                    System.out.printf("ID: %d, Title: %s, Category: %s, Amount: €%.2f, Date: %s%n",
                            expenseID, title, category, amount, dateIncurred);

                    // Add to total spend
                    totalSpend += amount;
                }

                // Print total spend
                System.out.printf("Total Spend: €%.2f%n", totalSpend);
            }
        } catch (DaoException e) {
            System.out.println("DaoException: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } finally {
            // Free the connection
            if (connection != null) {
                try {
                    dao.freeConnection(connection);
                } catch (DaoException e) {
                    System.out.println("Error freeing connection: " + e.getMessage());
                }
            }
        }
    }
}