package ua.lpnu.moneyobserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.moneyobserver.domain.Income;
import ua.lpnu.moneyobserver.domain.User;
import ua.lpnu.moneyobserver.service.IncomeService;
import ua.lpnu.moneyobserver.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/income")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> findUsersIncome(Principal principal){
        List<Income> income = incomeService.findAllInUser(principal.getName());
        return ResponseEntity.ok(income);
    }
    @GetMapping("/total")
    public ResponseEntity<?> getTotalIncome(Principal principal){
        Double totalIncome = incomeService.getTotalIncomeInUser(principal.getName());
        return ResponseEntity.ok(Map.of("totalIncome",totalIncome));
    }


    @PostMapping
    public ResponseEntity<?> createIncome(
            Principal principal,
            @RequestBody Income income
    ){
        User currentUser = userService.findByEmail(principal.getName());
        income.setUser(currentUser);
        Income createdIncome = incomeService.create(income);
        return ResponseEntity.ok(createdIncome);
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity<?> deleteIncome(@PathVariable Long incomeId){
        incomeService.delete(incomeId);
        return ResponseEntity.ok(null);
    }


}
