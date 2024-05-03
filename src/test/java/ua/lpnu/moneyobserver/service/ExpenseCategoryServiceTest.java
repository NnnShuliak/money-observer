package ua.lpnu.moneyobserver.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.lpnu.moneyobserver.dao.ExpenseCategoryRepository;
import ua.lpnu.moneyobserver.domain.ExpenseCategory;
import ua.lpnu.moneyobserver.domain.User;
import ua.lpnu.moneyobserver.dto.ExpenseCategoryDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ExpenseCategoryServiceTest {

    @MockBean
    private ExpenseCategoryRepository repository;
    @Autowired
    private ExpenseCategoryService service;

    @Test
    public void testFindAllInUser() {
        // Given
        String email = "test@example.com";
        Double totalIncome = 1000.0;
        when(repository.findTotalUserIncome(email)).thenReturn(totalIncome);
        when(repository.findAllByUser(email, totalIncome)).thenReturn(Collections.emptyList());

        // When
        List<ExpenseCategoryDTO> result = service.findAllInUser(email);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindById() {
        // Given
        Long id = 1L;
        ExpenseCategory category = new ExpenseCategory();
        category.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(category));

        // When
        ExpenseCategory result = service.findById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void testCreate() {
        // Given
        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setRatio(0.5);
        User user = new User();
        user.setId(1L); // Set a valid ID for the user
        expenseCategory.setUser(user); // Set the user for the expense category
        when(repository.save(expenseCategory)).thenReturn(expenseCategory);

        // When
        ExpenseCategory result = service.create(expenseCategory);

        // Then
        assertNotNull(result);
        assertEquals(expenseCategory, result);
    }

    @Test
    public void testGetSumOfAllRatiosInUser() {
        // Given
        Long userId = 1L;
        Double sum = 0.5;
        when(repository.sumOfAllRatios(userId)).thenReturn(sum);

        // When
        Double result = service.getSumOfAllRatiosInUser(userId);

        // Then
        assertNotNull(result);
        assertEquals(sum, result);
    }

    @Test
    public void testDelete() {
        // Given
        Long categoryId = 1L;

        // When
        service.delete(categoryId);

        // Then
        verify(repository, times(1)).deleteById(categoryId);
    }
}
