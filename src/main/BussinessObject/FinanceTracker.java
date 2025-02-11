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
            System.out.println("5. Add new income");
            System.out.println("6. Delete an income by ID");
            System.out.println("7. List income and expenses for a particular month");
            System.out.println("8. Exit");
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
                    addNewIncome();
                    break;
                case 6:
                    deleteIncomeById();
                    break;
                case 7:
                    listIncomeAndExpensesForMonth();
                    break;
                case 8:
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
            scanner.nextLine();


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
    public void addNewIncome() {
        Connection connection = null;
        try {
            connection = dao.getConnection();


            System.out.print("Enter Income title: ");
            String title = scanner.nextLine();
            System.out.print("Enter income amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter date (YYYY-MM-DD): ");
            String dateEarned = scanner.nextLine();


            String query = "INSERT INTO income (title, amount, dateEarned) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, title);
                pstmt.setDouble(2, amount);
                pstmt.setDate(3, Date.valueOf(dateEarned)); // Convert String to SQL Date
                pstmt.executeUpdate();
                System.out.println("Income added successfully!");
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
    public void deleteIncomeById() {
        Connection connection = null;
        try {
            connection = dao.getConnection();


            System.out.print("Enter the Income ID to delete: ");
            int incomeID = scanner.nextInt();
            scanner.nextLine();


            String query = "DELETE FROM income WHERE incomeID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, incomeID);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Income deleted successfully!");
                } else {
                    System.out.println("No income found with the given ID.");
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
    //So we need to input the Month, compare this with the month from data base
    //and if they are identical- output all other stuff
    public void listIncomeAndExpensesForMonth() {
        Connection connection = null;
        try {
            connection = dao.getConnection();


            System.out.print("Enter the month (MM): ");
            int month = scanner.nextInt();
            scanner.nextLine();

            // I feel pain in my brain because of it (@_@)
            String startDate = String.format("2025-%02d-01", month); // First day of the month
            String endDate = String.format("2025-%02d-31", month);   // Last day of the month

            // So we made an borders for our month dates here and now we are gonna call the data from the tables
            //from Income table
            double totalIncome = 0;
            String incomeQuery = "SELECT * FROM income WHERE dateEarned BETWEEN ? AND ?";
            try (PreparedStatement incomeStmt = connection.prepareStatement(incomeQuery)) {
                incomeStmt.setString(1, startDate);
                incomeStmt.setString(2, endDate);
                try (ResultSet incomeRs = incomeStmt.executeQuery()) {
                    System.out.println("\nIncome for: " + month + ":");
                    while (incomeRs.next()) {
                        int incomeID = incomeRs.getInt("incomeID");
                        String title = incomeRs.getString("title");
                        double amount = incomeRs.getDouble("amount");
                        Date dateEarned = incomeRs.getDate("dateEarned");

                        System.out.printf("ID: %d, Title: %s, Amount: €%.2f, Date: %s%n",
                                incomeID, title, amount, dateEarned);
                        totalIncome += amount;
                    }
                    System.out.printf("Total Income: €%.2f%n", totalIncome);
                }
            }

            // I don't want to do such things ;_;
            //The same for Expenses
            double totalExpenses = 0;
            String expenseQuery = "SELECT * FROM expense WHERE dateIncurred BETWEEN ? AND ?";
            try (PreparedStatement expenseStmt = connection.prepareStatement(expenseQuery)) {
                expenseStmt.setString(1, startDate);
                expenseStmt.setString(2, endDate);
                try (ResultSet expenseRs = expenseStmt.executeQuery()) {
                    System.out.println("\nExpenses for: " + month + ":");
                    while (expenseRs.next()) {
                        int expenseID = expenseRs.getInt("expenseID");
                        String title = expenseRs.getString("title");
                        String category = expenseRs.getString("category");
                        double amount = expenseRs.getDouble("amount");
                        Date dateIncurred = expenseRs.getDate("dateIncurred");

                        System.out.printf("ID: %d, Title: %s, Category: %s, Amount: €%.2f, Date: %s%n",
                                expenseID, title, category, amount, dateIncurred);
                        totalExpenses += amount;
                    }
                    System.out.printf("Total Expenses: €%.2f%n", totalExpenses);
                }
            }


            double remainingBalance = totalIncome - totalExpenses;
            System.out.printf("\nRemaining Balance: €%.2f%n", remainingBalance);

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
//I don't want to explain all the stuff ;_;