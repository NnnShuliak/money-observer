package ua.lpnu.moneyobserver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.lpnu.moneyobserver.dao.ExpenseCategoryRepository;
import ua.lpnu.moneyobserver.domain.ExpenseCategory;
import ua.lpnu.moneyobserver.dto.ExpenseCategoryDTO;
import ua.lpnu.moneyobserver.service.ExpenseCategoryService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {
    private final ExpenseCategoryRepository repository;

    @Override
    public List<ExpenseCategoryDTO> findAllInUser(String email) {
        Double totalIncome = repository.findTotalUserIncome(email);
        return repository.findAllByUser(email, totalIncome);
    }

    @Override
    public ExpenseCategory findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public ExpenseCategory create(ExpenseCategory expenseCategory) {
        double allRatios = getSumOfAllRatiosInUser(expenseCategory.getUser().getId());
        double availableRatio = 1 - allRatios;
        log.info("Available ratio = {}",availableRatio);
        if (availableRatio < expenseCategory.getRatio()) {
            throw new IllegalArgumentException(
                    String.format("ExpenseCategory ratio:%f is greater then available:%d", expenseCategory.getRatio(), availableRatio));
        }
        return repository.save(expenseCategory);
    }

    @Override
    public Double getSumOfAllRatiosInUser(Long userId) {
        return repository.sumOfAllRatios(userId);
    }

    @Override
    public void delete(Long categoryId) {
        repository.deleteById(categoryId);
    }
}
