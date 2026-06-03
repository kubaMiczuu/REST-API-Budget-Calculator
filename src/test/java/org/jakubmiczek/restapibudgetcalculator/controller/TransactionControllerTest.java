package org.jakubmiczek.restapibudgetcalculator.controller;

import org.jakubmiczek.restapibudgetcalculator.dto.TransactionRequest;
import org.jakubmiczek.restapibudgetcalculator.exception.GlobalExceptionHandler;
import org.jakubmiczek.restapibudgetcalculator.exception.TransactionDoesNotExistException;
import org.jakubmiczek.restapibudgetcalculator.model.TransactionCategory;
import org.jakubmiczek.restapibudgetcalculator.model.TransactionType;
import org.jakubmiczek.restapibudgetcalculator.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@Import(GlobalExceptionHandler.class)
public class TransactionControllerTest {

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturn201WhenCreateTransaction() throws Exception {
        TransactionRequest request = new TransactionRequest(BigDecimal.valueOf(200), TransactionType.INCOME, TransactionCategory.OTHER, null, LocalDate.now(), 1L);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn404WhenTransactionNotFound() throws Exception {

        doThrow(new TransactionDoesNotExistException(50L))
                .when(transactionService)
                        .deleteTransaction(50L);

        mockMvc.perform(delete("/api/transactions/"+50L))
                .andExpect(status().isNotFound());
    }
}
