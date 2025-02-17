package main.DAO;

import main.DTO.Income;
import main.Exceptions.DaoException;
import java.util.List;

public interface IncomeDAO {
    List<Income> findAllIncome() throws DaoException;
    void addIncome(Income income) throws DaoException;
    void deleteIncomeById(int incomeID) throws DaoException;
    List<Income> findIncomeForMonth(int year, int month) throws DaoException;
}