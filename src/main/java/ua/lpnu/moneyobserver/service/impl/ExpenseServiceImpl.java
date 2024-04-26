package ua.lpnu.moneyobserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lpnu.moneyobserver.dao.ExpenseRepository;
import ua.lpnu.moneyobserver.domain.Expense;
import ua.lpnu.moneyobserver.service.ExpenseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository repository;

    @Override
    public List<Expense> findAllInCategory(Long categoryId) {
        return repository.findAllByExpenseCategoryId(categoryId);
    }

    @Override
    public Expense create(Expense expense) {
        return repository.save(expense);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Double getTotalSpending(String email) {
        return repository.getAllExpensesInUser(email);

    }
}
