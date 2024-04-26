package ua.lpnu.moneyobserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.lpnu.moneyobserver.domain.ExpenseCategory;
import ua.lpnu.moneyobserver.domain.Income;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income,Long> {

    @Query ("SELECT COALESCE(SUM(i.amount), 0) FROM Income i where i.user.email= :email")
    Double getAllUsersIncome(String email);
    List<Income> findAllByUserEmail(String email);

}
