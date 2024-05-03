package ua.lpnu.moneyobserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.moneyobserver.domain.Expense;
import ua.lpnu.moneyobserver.domain.ExpenseCategory;
import ua.lpnu.moneyobserver.service.ExpenseService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;


    @GetMapping("/categories/{categoryId}/expenses")
    public ResponseEntity<?> findAllInCategory(@PathVariable Long categoryId){
        log.info("Expanses searching in category id:{}",categoryId);
        List<Expense> expenses = expenseService.findAllInCategory(categoryId);
        return ResponseEntity.ok(expenses);
    }
    @GetMapping("/expenses/totalSpending")
    public ResponseEntity<?> getTotalUserSpending(Principal principal){
        Double totalSpending = expenseService.getTotalSpending(principal.getName());
        return ResponseEntity.ok(Map.of("totalSpending",totalSpending));
    }

    @PostMapping("/categories/{categoryId}/expenses")
    public ResponseEntity<?> create(
            @PathVariable Long categoryId,
            @RequestBody Expense expense
    ){
        ExpenseCategory category = new ExpenseCategory();
        category.setId(categoryId);
        expense.setExpenseCategory(category);

        Expense createdExpense = expenseService.create(expense);
        return ResponseEntity.ok(createdExpense);
    }
    @DeleteMapping("/expenses/{expenseId}")
    public ResponseEntity<?> delete(@PathVariable Long expenseId){
        expenseService.delete(expenseId);
        return ResponseEntity.ok(null);
    }

}
