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
import ua.lpnu.moneyobserver.domain.Income;
import ua.lpnu.moneyobserver.domain.User;
import ua.lpnu.moneyobserver.security.JwtCore;
import ua.lpnu.moneyobserver.service.IncomeService;
import ua.lpnu.moneyobserver.service.UserService;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IncomeController.class)
@Import({JwtCore.class, UserService.class})
@AutoConfigureMockMvc(addFilters = false)
public class IncomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncomeService incomeService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    private final String USER_EMAIL = "test@example.com";

    @Test
    public void testFindUsersIncome() throws Exception {
        // Given
        String userEmail = "test@example.com";
        Principal principal = () -> userEmail;
        List<Income> incomes = List.of(new Income(), new Income());
        when(incomeService.findAllInUser(userEmail)).thenReturn(incomes);

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/income")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(incomes.size()));
    }

    @Test
    public void testGetTotalIncome() throws Exception {
        // Given
        String userEmail = "test@example.com";
        Principal principal = () -> userEmail;
        Double totalIncome = 1000.0;
        when(incomeService.getTotalIncomeInUser(userEmail)).thenReturn(totalIncome);

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/income/total")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalIncome").value(totalIncome));
    }

    @Test
    public void testCreateIncome() throws Exception {
        // Given
        String userEmail = "test@example.com";
        Principal principal = () -> userEmail;
        User currentUser = new User();
        currentUser.setEmail(userEmail);

        Income income = new Income();
        income.setDescription("Test Income");
        income.setAmount(1000.0);

        income.setTime(new Timestamp(System.currentTimeMillis()));

        when(userService.findByEmail(userEmail)).thenReturn(currentUser);
        when(incomeService.create(any())).thenReturn(income.setUser(currentUser));
        income.setUser(null);

        // Create an Income object


        // When calling the createIncome endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/api/income")
                        .content(asJsonString(income))
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testDeleteIncome() throws Exception {
        // Given
        Long incomeId = 1L;

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/income/{incomeId}", incomeId))
                .andExpect(status().isOk());

        verify(incomeService, times(1)).delete(incomeId);
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
