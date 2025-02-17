package main.BussinessObject;

import main.DAO.ExpensesDAO;
import main.DAO.IncomeDAO;
import main.DAO.MySqlDao;
import main.DAO.MySQLExpensesDAO;
import main.DAO.MySQLIncomeDAO;
import main.DTO.Expense;
import main.DTO.Income;
import main.Exceptions.DaoException;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class FinanceTracker {

    private MySqlDao dao;
    private MySQLExpensesDAO expensesDAO;
    private MySQLIncomeDAO incomeDAO;
    private Scanner scanner;

    public FinanceTracker() throws DaoException {
        dao = new MySqlDao();
        Connection connection = dao.getConnection();
        expensesDAO = new MySQLExpensesDAO(connection);
        incomeDAO = new MySQLIncomeDAO(connection);
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        try {
            FinanceTracker tracker = new FinanceTracker();
            tracker.run();
        } catch (DaoException e) {
            System.out.println("Error initializing FinanceTracker: " + e.getMessage());
        }
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
            scanner.nextLine();

            try {
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
            } catch (DaoException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void listAllExpenses() throws DaoException {
        List<Expense> expenses = expensesDAO.findAllExpenses();
        double totalSpend = 0;
        System.out.println("\nExpenses:");
        for (Expense expense : expenses) {
            System.out.printf("ID: %d, Title: %s, Category: %s, Amount: €%.2f, Date: %s%n",
                    expense.getExpenseID(), expense.getTitle(), expense.getCategory(),
                    expense.getAmount(), expense.getDateIncurred());
            totalSpend += expense.getAmount();
        }
        System.out.printf("Total Spend: €%.2f%n", totalSpend);
    }

    private void addNewExpense() throws DaoException {
        System.out.print("Enter expense title: ");
        String title = scanner.nextLine();
        System.out.print("Enter expense category: ");
        String category = scanner.nextLine();
        System.out.print("Enter expense amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter date incurred (YYYY-MM-DD): ");
        String dateIncurred = scanner.nextLine();

        Expense expense = new Expense();
        expense.setTitle(title);
        expense.setCategory(category);
        expense.setAmount(amount);
        expense.setDateIncurred(java.sql.Date.valueOf(dateIncurred));

        expensesDAO.addExpense(expense);
        System.out.println("Expense added successfully!");
    }

    private void deleteExpenseById() throws DaoException {
        System.out.print("Enter the expense ID to delete: ");
        int expenseID = scanner.nextInt();
        scanner.nextLine();

        expensesDAO.deleteExpenseById(expenseID);
        System.out.println("Expense deleted successfully!");
    }

    private void listAllIncome() throws DaoException {
        List<Income> incomeList = incomeDAO.findAllIncome();
        double totalIncome = 0;
        System.out.println("\nIncome:");
        for (Income income : incomeList) {
            System.out.printf("ID: %d, Title: %s, Amount: €%.2f, Date: %s%n",
                    income.getIncomeID(), income.getTitle(), income.getAmount(), income.getDateEarned());
            totalIncome += income.getAmount();
        }
        System.out.printf("Total Income: €%.2f%n", totalIncome);
    }

    private void addNewIncome() throws DaoException {
        System.out.print("Enter income title: ");
        String title = scanner.nextLine();
        System.out.print("Enter income amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter date earned (YYYY-MM-DD): ");
        String dateEarned = scanner.nextLine();

        Income income = new Income();
        income.setTitle(title);
        income.setAmount(amount);
        income.setDateEarned(java.sql.Date.valueOf(dateEarned));

        incomeDAO.addIncome(income);
        System.out.println("Income added successfully!");
    }

    private void deleteIncomeById() throws DaoException {
        System.out.print("Enter the income ID to delete: ");
        int incomeID = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        incomeDAO.deleteIncomeById(incomeID);
        System.out.println("Income deleted successfully!");
    }

    private void listIncomeAndExpensesForMonth() throws DaoException {
        System.out.print("Enter the year (YYYY): ");
        int year = scanner.nextInt();
        System.out.print("Enter the month (MM): ");
        int month = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Fetch income for the month
        List<Income> incomeList = incomeDAO.findIncomeForMonth(year, month);
        double totalIncome = 0;
        System.out.println("\nIncome for " + year + "-" + month + ":");
        for (Income income : incomeList) {
            System.out.printf("ID: %d, Title: %s, Amount: €%.2f, Date: %s%n",
                    income.getIncomeID(), income.getTitle(), income.getAmount(), income.getDateEarned());
            totalIncome += income.getAmount();
        }
        System.out.printf("Total Income: €%.2f%n", totalIncome);

        // Fetch expenses for the month
        List<Expense> expenses = expensesDAO.findExpensesForMonth(year, month);
        double totalExpenses = 0;
        System.out.println("\nExpenses for " + year + "-" + month + ":");
        for (Expense expense : expenses) {
            System.out.printf("ID: %d, Title: %s, Category: %s, Amount: €%.2f, Date: %s%n",
                    expense.getExpenseID(), expense.getTitle(), expense.getCategory(),
                    expense.getAmount(), expense.getDateIncurred());
            totalExpenses += expense.getAmount();
        }
        System.out.printf("Total Expenses: €%.2f%n", totalExpenses);


        double remainingBalance = totalIncome - totalExpenses;
        System.out.printf("\nRemaining Balance: €%.2f%n", remainingBalance);
    }
}