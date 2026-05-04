package pl.edu.agh.to.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.dto.StockDto;
import pl.edu.agh.to.dto.TradeRequestDto;
import pl.edu.agh.to.dto.WalletStateDto;
import pl.edu.agh.to.service.BankQueryService;
import pl.edu.agh.to.service.TradingService;

import java.util.List;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final TradingService tradingService;
    private final BankQueryService bankQueryService;

    @PostMapping("/{wallet_id}/stocks/{stock_name}")
    public ResponseEntity<Void> trade(
            @PathVariable("wallet_id") String walletId,
            @PathVariable("stock_name") String stockName,
            @Valid @RequestBody TradeRequestDto request) {

        tradingService.processTrade(walletId, stockName, request.getType());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{wallet_id}")
    public ResponseEntity<WalletStateDto> getWallet(@PathVariable("wallet_id") String walletId) {
        List<StockDto> stocks = bankQueryService.getWalletState(walletId).stream()
                .map(w -> new StockDto(w.getStockName(), w.getQuantity()))
                .toList();

        return ResponseEntity.ok(new WalletStateDto(walletId, stocks));
    }

    @GetMapping("/{wallet_id}/stocks/{stock_name}")
    public ResponseEntity<Integer> getWalletStock(
            @PathVariable("wallet_id") String walletId,
            @PathVariable("stock_name") String stockName) {

        int quantity = bankQueryService.getWalletStockQuantity(walletId, stockName);
        return ResponseEntity.ok(quantity);
    }
}