package main.DAO;

import main.DTO.Expense;
import main.Exceptions.DaoException;
import java.util.List;

public interface ExpensesDAO {
    List<Expense> findAllExpenses() throws DaoException;
    void addExpense(Expense expense) throws DaoException;
    void deleteExpenseById(int expenseID) throws DaoException;
    List<Expense> findExpensesForMonth(int year, int month) throws DaoException;
}