package ua.lpnu.moneyobserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.moneyobserver.domain.ExpenseCategory;
import ua.lpnu.moneyobserver.domain.User;
import ua.lpnu.moneyobserver.dto.ExpenseCategoryDTO;
import ua.lpnu.moneyobserver.service.ExpenseCategoryService;
import ua.lpnu.moneyobserver.service.UserService;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class ExpanseCategoryController {
    private final UserService userService;

    private final ExpenseCategoryService expenseCategoryService;

    @GetMapping
    public ResponseEntity<?> findAll(Principal principal){

        String username = principal.getName();
        log.info("{} find all categories",username);

        List<ExpenseCategoryDTO> expenseCategories = expenseCategoryService.findAllInUser(username);
        return ResponseEntity.ok(expenseCategories);
    }


    @PostMapping
    public ResponseEntity<?> createExpenseCategory(@RequestBody ExpenseCategory expenseCategory, Principal principal){
        String username = principal.getName();
        log.info("{} create category",username);
        User currentUser = userService.findByEmail(username);
        expenseCategory.setUser(currentUser);
        ExpenseCategory savedExpenseCategory = expenseCategoryService.create(expenseCategory);
        return ResponseEntity.ok(savedExpenseCategory);
    }
    @DeleteMapping("/{category_id}")
    public ResponseEntity<?> delete(@PathVariable("category_id") Long category_id){

        expenseCategoryService.delete(category_id);
        return ResponseEntity.ok(null);
    }


}
