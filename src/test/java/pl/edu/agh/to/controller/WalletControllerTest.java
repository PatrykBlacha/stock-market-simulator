package pl.edu.agh.to.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.edu.agh.to.dto.TradeRequestDto;
import pl.edu.agh.to.entity.OperationType;
import pl.edu.agh.to.exception.ApiErrorCode;
import pl.edu.agh.to.exception.ApiException;
import pl.edu.agh.to.service.BankQueryService;
import pl.edu.agh.to.service.TradingService;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TradingService tradingService;

    @MockBean
    private BankQueryService bankQueryService;

    @Test
    void shouldReturn200WhenTradeIsSuccessful() throws Exception {
        TradeRequestDto request = new TradeRequestDto();
        request.setType(OperationType.BUY);

        mockMvc.perform(post("/wallets/wallet1/stocks/AAPL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenStockDoesNotExist() throws Exception {
        TradeRequestDto request = new TradeRequestDto();
        request.setType(OperationType.BUY);

        doThrow(new ApiException(ApiErrorCode.STOCK_NOT_FOUND))
                .when(tradingService).processTrade("wallet1", "INVALID_STOCK", OperationType.BUY);

        mockMvc.perform(post("/wallets/wallet1/stocks/INVALID_STOCK")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("STOCK_NOT_FOUND"));
    }

    @Test
    void shouldReturn400WhenNoStockInBank() throws Exception {
        TradeRequestDto request = new TradeRequestDto();
        request.setType(OperationType.BUY);

        doThrow(new ApiException(ApiErrorCode.INSUFFICIENT_BANK_STOCK))
                .when(tradingService).processTrade("wallet1", "AAPL", OperationType.BUY);

        mockMvc.perform(post("/wallets/wallet1/stocks/AAPL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INSUFFICIENT_BANK_STOCK"));
    }
}