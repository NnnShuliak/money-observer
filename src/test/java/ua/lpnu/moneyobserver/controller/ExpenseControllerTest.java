package ua.lpnu.moneyobserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.lpnu.moneyobserver.domain.Expense;
import ua.lpnu.moneyobserver.domain.ExpenseCategory;
import ua.lpnu.moneyobserver.security.JwtCore;
import ua.lpnu.moneyobserver.service.ExpenseService;
import ua.lpnu.moneyobserver.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseController.class)
@Import({JwtCore.class})
@AutoConfigureMockMvc(addFilters = false)
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExpenseService expenseService;

    private final String USER_EMAIL = "test@example.com";


    @Test
    public void testFindAllInCategory() throws Exception {
        // Given
        Long categoryId = 1L;
        List<Expense> expenses = List.of(new Expense(), new Expense());
        when(expenseService.findAllInCategory(categoryId)).thenReturn(expenses);

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/{categoryId}/expenses", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(expenses.size()));
    }

    @Test
    public void testGetTotalUserSpending() throws Exception {
        // Given
        Double totalSpending = 1000.0;
        when(expenseService.getTotalSpending(anyString())).thenReturn(totalSpending);

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/expenses/totalSpending").with(request -> {
                    request.setUserPrincipal(() -> USER_EMAIL);
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalSpending").value(totalSpending));
    }

    @Test
    public void testCreate() throws Exception {
        // Given
        Long categoryId = 1L;
        Expense expense = new Expense();
        ExpenseCategory category = new ExpenseCategory();
        category.setId(categoryId);
        expense.setExpenseCategory(category);
        when(expenseService.create(any(Expense.class))).thenReturn(expense);

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories/{categoryId}/expenses", categoryId)
                        .content(asJsonString(expense))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.expenseCategory.id").value(categoryId));
    }

    @Test
    public void testDelete() throws Exception {
        // Given
        Long expenseId = 1L;

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/expenses/{expenseId}", expenseId))
                .andExpect(status().isOk());

        verify(expenseService, times(1)).delete(expenseId);
    }

    // Helper method to convert object to JSON string
    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
