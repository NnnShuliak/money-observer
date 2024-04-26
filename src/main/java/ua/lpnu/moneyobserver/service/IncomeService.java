package ua.lpnu.moneyobserver.service;

import ua.lpnu.moneyobserver.domain.Income;

import java.util.List;

public interface IncomeService {
    List<Income> findAllInUser(String email);

    Income create(Income income);

    Double getTotalIncomeInUser(String email);

    void delete(Long id);
}
