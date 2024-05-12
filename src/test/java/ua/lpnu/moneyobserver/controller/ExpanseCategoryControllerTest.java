package ua.lpnu.moneyobserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.lpnu.moneyobserver.domain.ExpenseCategory;
import ua.lpnu.moneyobserver.domain.User;
import ua.lpnu.moneyobserver.dto.ExpenseCategoryDTO;
import ua.lpnu.moneyobserver.security.JwtCore;
import ua.lpnu.moneyobserver.service.ExpenseCategoryService;
import ua.lpnu.moneyobserver.service.UserService;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ExpanseCategoryController.class)
@Import({JwtCore.class, UserService.class})
@AutoConfigureMockMvc(addFilters = false)
public class ExpanseCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private ExpenseCategoryService expenseCategoryService;

    private final String USER_EMAIL = "test@example.com";


    @BeforeEach
    public void setup() {
        when(userService.findByEmail(USER_EMAIL)).thenReturn(new User());
        when(expenseCategoryService.findAllInUser(USER_EMAIL)).thenReturn(Collections.singletonList(
                new ExpenseCategoryDTO(1L, "Test Category", 0.0, 0.0)
        ));
    }

    @Test
    public void testFindAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories").with(request -> {
                    request.setUserPrincipal(() -> USER_EMAIL);
                    return request;
                }))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Category"));
    }

    @Test
    public void testCreateExpenseCategory() throws Exception {
        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setTitle("Test Category");
        expenseCategory.setRatio(0.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories").with(request -> {
                            request.setUserPrincipal(() -> USER_EMAIL);
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseCategory)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/1"))
                .andExpect(status().isOk());
    }


}

