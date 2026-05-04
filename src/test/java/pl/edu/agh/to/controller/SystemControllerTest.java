package pl.edu.agh.to.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.edu.agh.to.entity.AuditLog;
import pl.edu.agh.to.entity.OperationType;
import pl.edu.agh.to.service.BankQueryService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SystemController.class)
class SystemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankQueryService bankQueryService;

    @Test
    void shouldReturnAuditLogCorrectlyFormatted() throws Exception {
        AuditLog log1 = new AuditLog(OperationType.BUY, "w1", "AAPL");
        AuditLog log2 = new AuditLog(OperationType.SELL, "w2", "GOOGL");

        when(bankQueryService.getAuditLog()).thenReturn(List.of(log1, log2));

        mockMvc.perform(get("/log"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.log").isArray())
                .andExpect(jsonPath("$.log[0].type").value("buy"))
                .andExpect(jsonPath("$.log[0].wallet_id").value("w1"))
                .andExpect(jsonPath("$.log[0].stock_name").value("AAPL"))
                .andExpect(jsonPath("$.log[1].type").value("sell"))
                .andExpect(jsonPath("$.log[1].wallet_id").value("w2"))
                .andExpect(jsonPath("$.log[1].stock_name").value("GOOGL"));
    }
}