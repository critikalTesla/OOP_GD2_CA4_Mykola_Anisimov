package main.DAO;

import main.DTO.Income;
import main.Exceptions.DaoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLIncomeDAO implements IncomeDAO {

    private Connection connection;

    public MySQLIncomeDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Income> findAllIncome() throws DaoException {
        List<Income> incomeList = new ArrayList<>();
        String query = "SELECT * FROM income";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Income income = new Income();
                income.setIncomeID(rs.getInt("incomeID"));
                income.setTitle(rs.getString("title"));
                income.setAmount(rs.getDouble("amount"));
                income.setDateEarned(rs.getDate("dateEarned"));
                incomeList.add(income);
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching income: " + e.getMessage());
        }
        return incomeList;
    }

    @Override
    public void addIncome(Income income) throws DaoException {
        String query = "INSERT INTO income (title, amount, dateEarned) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, income.getTitle());
            pstmt.setDouble(2, income.getAmount());
            pstmt.setDate(3, new java.sql.Date(income.getDateEarned().getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error adding income: " + e.getMessage());
        }
    }

    @Override
    public void deleteIncomeById(int incomeID) throws DaoException {
        String query = "DELETE FROM income WHERE incomeID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, incomeID);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DaoException("No income found with ID: " + incomeID);
            }
        } catch (SQLException e) {
            throw new DaoException("Error deleting income: " + e.getMessage());
        }
    }

    @Override
    public List<Income> findIncomeForMonth(int year, int month) throws DaoException {
        List<Income> incomeList = new ArrayList<>();
        String startDate = String.format("%04d-%02d-01", year, month);
        String endDate = String.format("%04d-%02d-31", year, month);

        String query = "SELECT * FROM income WHERE dateEarned BETWEEN ? AND ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Income income = new Income();
                    income.setIncomeID(rs.getInt("incomeID"));
                    income.setTitle(rs.getString("title"));
                    income.setAmount(rs.getDouble("amount"));
                    income.setDateEarned(rs.getDate("dateEarned"));
                    incomeList.add(income);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching income for month: " + e.getMessage());
        }
        return incomeList;
    }
}