package org.jakubmiczek.restapibudgetcalculator.controller;

import org.jakubmiczek.restapibudgetcalculator.dto.AccountRequest;
import org.jakubmiczek.restapibudgetcalculator.exception.AccountCouldNotBeDeletedException;
import org.jakubmiczek.restapibudgetcalculator.exception.AccountDoesNotExistException;
import org.jakubmiczek.restapibudgetcalculator.exception.GlobalExceptionHandler;
import org.jakubmiczek.restapibudgetcalculator.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@Import(GlobalExceptionHandler.class)
public class AccountControllerTest {

    @MockitoBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn201WhenCreateAccount() throws Exception {
        AccountRequest request = new AccountRequest("Savings");

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn404WhenNoAccountFound() throws Exception {
        when(accountService.getAccountDetails(50L)).thenThrow(new AccountDoesNotExistException(50L));

        mockMvc.perform(get("/api/accounts/50"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn409WhenTryingToDeleteAccount() throws Exception {
        doThrow(new AccountCouldNotBeDeletedException(1L))
                .when(accountService)
                        .deleteAccount(1L);

        mockMvc.perform(delete("/api/accounts/" + 1L))
                .andExpect(status().isConflict());
    }
}
