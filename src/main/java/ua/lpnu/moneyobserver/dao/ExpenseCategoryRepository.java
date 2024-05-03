package ua.lpnu.moneyobserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.lpnu.moneyobserver.domain.ExpenseCategory;
import ua.lpnu.moneyobserver.dto.ExpenseCategoryDTO;

import java.util.List;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory,Long> {


    List<ExpenseCategory> findAllByUserEmail(String email);
    @Query("select sum(i.amount) from Income i where i.user.email = :email ")
    Double findTotalUserIncome(String email);
    @Query("SELECT new ua.lpnu.moneyobserver.dto.ExpenseCategoryDTO(ec.id, ec.title, COALESCE(SUM(e.amount),0),ec.ratio * :totalIncome) " +
            "FROM ExpenseCategory ec " +
            "LEFT JOIN ec.expenses e " +
            "WHERE  ec.user.email = :email " +
            "GROUP BY ec.id, ec.title, ec.ratio")
    List<ExpenseCategoryDTO> findAllByUser(String email, Double totalIncome);

    @Query("SELECT COALESCE(SUM(ec.ratio), 0) FROM ExpenseCategory ec where ec.user.id= :userId")
    Double sumOfAllRatios(Long userId);
}
