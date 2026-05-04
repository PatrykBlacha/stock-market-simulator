package pl.edu.agh.to.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.dto.StockDto;
import pl.edu.agh.to.dto.StockListDto;
import pl.edu.agh.to.entity.BankStock;
import pl.edu.agh.to.service.BankQueryService;

import java.util.List;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class BankController {

    private final BankQueryService bankQueryService;

    @GetMapping
    public ResponseEntity<StockListDto> getBankStocks() {
        List<StockDto> stocks = bankQueryService.getBankState().stream()
                .map(b -> new StockDto(b.getName(), b.getQuantity()))
                .toList();

        StockListDto response = new StockListDto();
        response.setStocks(stocks);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> setBankStocks(@RequestBody StockListDto request) {
        List<BankStock> newStocks = request.getStocks().stream()
                .map(dto -> new BankStock(dto.getName(), dto.getQuantity()))
                .toList();

        bankQueryService.setBankState(newStocks);
        return ResponseEntity.ok().build();
    }
}