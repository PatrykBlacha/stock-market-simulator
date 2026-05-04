package pl.edu.agh.to.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.edu.agh.to.dto.StockDto;
import pl.edu.agh.to.dto.StockListDto;
import pl.edu.agh.to.entity.BankStock;
import pl.edu.agh.to.service.BankQueryService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankController.class)
class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BankQueryService bankQueryService;

    @Test
    void shouldReturnCurrentBankState() throws Exception {
        when(bankQueryService.getBankState())
                .thenReturn(List.of(new BankStock("AAPL", 100), new BankStock("GOOGL", 50)));

        mockMvc.perform(get("/stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stocks").isArray()) // Sprawdzamy, czy "stocks" to tablica
                .andExpect(jsonPath("$.stocks[0].name").value("AAPL"))
                .andExpect(jsonPath("$.stocks[0].quantity").value(100))
                .andExpect(jsonPath("$.stocks[1].name").value("GOOGL"))
                .andExpect(jsonPath("$.stocks[1].quantity").value(50));
    }

    @Test
    void shouldSetBankStateAndReturn200() throws Exception {
        StockListDto request = new StockListDto();
        request.setStocks(List.of(new StockDto("TSLA", 200)));

        mockMvc.perform(post("/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(bankQueryService).setBankState(List.of(new BankStock("TSLA", 200)));
    }
}