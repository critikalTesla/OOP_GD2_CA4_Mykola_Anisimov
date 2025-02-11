package main.BussinessObject;
//@]{/Xxx_Savage_Mykola_xxX\}[@
import main.DAO.MySqlDao;
import main.Exceptions.DaoException;
import java.sql.*;
import java.util.Scanner;

public class FinanceTracker {

    private MySqlDao dao;
    private Scanner scanner;

    public FinanceTracker() {
        dao = new MySqlDao();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        FinanceTracker tracker = new FinanceTracker();
        tracker.run();
    }

    public void run() {
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. List all expenses and calculate total spend");
            System.out.println("2. Add a new expense");
            System.out.println("3. Delete an expense by ID");
            System.out.println("4. List all income and calculate total income");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    listAllExpenses();
                    break;
                case 2:
                    addNewExpense();
                    break;
                case 3:
                    deleteExpenseById();
                    break;
                case 4:
                    listAllIncome();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    public void listAllExpenses() {
        Connection connection = null;
        try {
            connection = dao.getConnection();
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

                    System.out.printf("ID: %d, Title: %s, Category: %s, Amount: €%.2f, Date: %s%n",
                            expenseID, title, category, amount, dateIncurred);
                    totalSpend += amount;
                }
                System.out.printf("Total Spend: €%.2f%n", totalSpend);
            }
        } catch (DaoException e) {
            System.out.println("DaoException: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    dao.freeConnection(connection);
                } catch (DaoException e) {
                    System.out.println("Error freeing connection: " + e.getMessage());
                }
            }
        }
    }


    public void addNewExpense() {
        Connection connection = null;
        try {
            connection = dao.getConnection();


            System.out.print("Enter expense title: ");
            String title = scanner.nextLine();
            System.out.print("Enter expense category: ");
            String category = scanner.nextLine();
            System.out.print("Enter expense amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter date incurred (YYYY-MM-DD): ");
            String dateIncurred = scanner.nextLine();


            String query = "INSERT INTO expense (title, category, amount, dateIncurred) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, title);
                pstmt.setString(2, category);
                pstmt.setDouble(3, amount);
                pstmt.setDate(4, Date.valueOf(dateIncurred)); // Convert String to SQL Date
                pstmt.executeUpdate();
                System.out.println("Expense added successfully!");
            }
        } catch (DaoException e) {
            System.out.println("DaoException: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        } finally {
            if (connection != null) {
                try {
                    dao.freeConnection(connection);
                } catch (DaoException e) {
                    System.out.println("Error freeing connection: " + e.getMessage());
                }
            }
        }
    }


    public void deleteExpenseById() {
        Connection connection = null;
        try {
            connection = dao.getConnection();


            System.out.print("Enter the expense ID to delete: ");
            int expenseID = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // SQL query to delete the expense
            String query = "DELETE FROM expense WHERE expenseID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, expenseID);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Expense deleted successfully!");
                } else {
                    System.out.println("No expense found with the given ID.");
                }
            }
        } catch (DaoException e) {
            System.out.println("DaoException: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    dao.freeConnection(connection);
                } catch (DaoException e) {
                    System.out.println("Error freeing connection: " + e.getMessage());
                }
            }
        }
    }


    public void listAllIncome() {
        Connection connection = null;
        try {
            connection = dao.getConnection();


            String query = "SELECT * FROM income";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                double totalIncome = 0;
                System.out.println("\nIncome:");
                while (rs.next()) {
                    int incomeID = rs.getInt("incomeID");
                    String title = rs.getString("title");
                    double amount = rs.getDouble("amount");
                    Date dateEarned = rs.getDate("dateEarned");


                    System.out.printf("ID: %d, Title: %s, Amount: €%.2f, Date: %s%n",
                            incomeID, title, amount, dateEarned);


                    totalIncome += amount;
                }


                System.out.printf("Total Income: €%.2f%n", totalIncome);
            }
        } catch (DaoException e) {
            System.out.println("DaoException: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } finally {
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