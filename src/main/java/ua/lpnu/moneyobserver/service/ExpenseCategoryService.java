package ua.lpnu.moneyobserver.service;

import ua.lpnu.moneyobserver.domain.ExpenseCategory;
import ua.lpnu.moneyobserver.dto.ExpenseCategoryDTO;

import java.util.List;

public interface ExpenseCategoryService {
    List<ExpenseCategoryDTO> findAllInUser(String email);

    ExpenseCategory findById(Long id);

    ExpenseCategory create(ExpenseCategory expenseCategory);

     Integer getSumOfAllRatiosInUser(Long userId);

    void delete(Long categoryId);
}
