package main.DAO;

import main.DTO.Expense;
import main.Exceptions.DaoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLExpensesDAO implements ExpensesDAO {

    private Connection connection;

    public MySQLExpensesDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Expense> findAllExpenses() throws DaoException {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT * FROM expense";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Expense expense = new Expense();
                expense.setExpenseID(rs.getInt("expenseID"));
                expense.setTitle(rs.getString("title"));
                expense.setCategory(rs.getString("category"));
                expense.setAmount(rs.getDouble("amount"));
                expense.setDateIncurred(rs.getDate("dateIncurred"));
                expenses.add(expense);
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching expenses: " + e.getMessage());
        }
        return expenses;
    }

    @Override
    public void addExpense(Expense expense) throws DaoException {
        String query = "INSERT INTO expense (title, category, amount, dateIncurred) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, expense.getTitle());
            pstmt.setString(2, expense.getCategory());
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setDate(4, new java.sql.Date(expense.getDateIncurred().getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error adding expense: " + e.getMessage());
        }
    }

    @Override
    public void deleteExpenseById(int expenseID) throws DaoException {
        String query = "DELETE FROM expense WHERE expenseID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, expenseID);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DaoException("No expense found with ID: " + expenseID);
            }
        } catch (SQLException e) {
            throw new DaoException("Error deleting expense: " + e.getMessage());
        }
    }

    @Override
    public List<Expense> findExpensesForMonth(int year, int month) throws DaoException {
        List<Expense> expenses = new ArrayList<>();
        String startDate = String.format("%04d-%02d-01", year, month);
        String endDate = String.format("%04d-%02d-31", year, month);

        String query = "SELECT * FROM expense WHERE dateIncurred BETWEEN ? AND ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Expense expense = new Expense();
                    expense.setExpenseID(rs.getInt("expenseID"));
                    expense.setTitle(rs.getString("title"));
                    expense.setCategory(rs.getString("category"));
                    expense.setAmount(rs.getDouble("amount"));
                    expense.setDateIncurred(rs.getDate("dateIncurred"));
                    expenses.add(expense);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching expenses for month: " + e.getMessage());
        }
        return expenses;
    }
}