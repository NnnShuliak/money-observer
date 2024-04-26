package ua.lpnu.moneyobserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.lpnu.moneyobserver.domain.Expense;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {


    List<Expense> findAllByExpenseCategoryId(Long expenseCategoryId);
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.expenseCategory.user.email=:email")
    Double getAllExpensesInUser(String email);


}
