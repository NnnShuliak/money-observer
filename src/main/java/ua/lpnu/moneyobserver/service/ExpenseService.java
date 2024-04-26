package ua.lpnu.moneyobserver.service;

import ua.lpnu.moneyobserver.domain.Expense;

import java.util.List;

public interface ExpenseService {
    List<Expense> findAllInCategory(Long categoryId);

    Expense create(Expense expense);

    void delete(Long id);

    Double getTotalSpending(String email);
}
