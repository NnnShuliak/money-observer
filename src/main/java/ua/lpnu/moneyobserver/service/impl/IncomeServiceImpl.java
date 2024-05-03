package ua.lpnu.moneyobserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lpnu.moneyobserver.dao.IncomeRepository;
import ua.lpnu.moneyobserver.domain.Income;
import ua.lpnu.moneyobserver.service.IncomeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository repository;
    @Override
    public List<Income> findAllInUser(String email){
        return repository.findAllByUserEmail(email);
    }
    @Override
    public Income create(Income income){
        return repository.save(income);
    }
    @Override
    public Double getTotalIncomeInUser(String email){
        return repository.getAllUsersIncome(email);
    }
    @Override
    public void delete(Long id){
        repository.deleteById(id);
    }



}
