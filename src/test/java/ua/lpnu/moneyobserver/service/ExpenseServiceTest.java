package ua.lpnu.moneyobserver.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.lpnu.moneyobserver.dao.ExpenseRepository;
import ua.lpnu.moneyobserver.domain.Expense;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ExpenseServiceTest {

    @MockBean
    private ExpenseRepository repository;

    @Autowired
    private ExpenseService service;

    @Test
    public void testFindAllInCategory() {
        // Given
        Long categoryId = 1L;
        when(repository.findAllByExpenseCategoryId(categoryId)).thenReturn(Collections.emptyList());

        // When
        List<Expense> result = service.findAllInCategory(categoryId);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCreate() {
        // Given
        Expense expense = new Expense();
        when(repository.save(expense)).thenReturn(expense);

        // When
        Expense result = service.create(expense);

        // Then
        assertNotNull(result);
        assertEquals(expense, result);
    }

    @Test
    public void testDelete() {
        // Given
        Long id = 1L;

        // When
        service.delete(id);

        // Then
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    public void testGetTotalSpending() {
        // Given
        String email = "test@example.com";
        Double totalSpending = 1000.0;
        when(repository.getAllExpensesInUser(email)).thenReturn(totalSpending);

        // When
        Double result = service.getTotalSpending(email);

        // Then
        assertNotNull(result);
        assertEquals(totalSpending, result);
    }
}
