package ua.lpnu.moneyobserver.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.lpnu.moneyobserver.dao.IncomeRepository;
import ua.lpnu.moneyobserver.domain.Income;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class IncomeServiceTest {

    @MockBean
    private IncomeRepository repository;

    @Autowired
    private IncomeService service;

    @Test
    public void testFindAllInUser() {
        // Given
        String email = "test@example.com";
        when(repository.findAllByUserEmail(email)).thenReturn(Collections.emptyList());

        // When
        List<Income> result = service.findAllInUser(email);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCreate() {
        // Given
        Income income = new Income();
        when(repository.save(income)).thenReturn(income);

        // When
        Income result = service.create(income);

        // Then
        assertNotNull(result);
        assertEquals(income, result);
    }

    @Test
    public void testGetTotalIncomeInUser() {
        // Given
        String email = "test@example.com";
        Double totalIncome = 1000.0;
        when(repository.getAllUsersIncome(email)).thenReturn(totalIncome);

        // When
        Double result = service.getTotalIncomeInUser(email);

        // Then
        assertNotNull(result);
        assertEquals(totalIncome, result);
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
}
