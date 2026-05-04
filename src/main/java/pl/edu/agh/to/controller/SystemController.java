package pl.edu.agh.to.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.dto.LogEntryDto;
import pl.edu.agh.to.dto.LogListDto;
import pl.edu.agh.to.service.BankQueryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SystemController {

    private final BankQueryService bankQueryService;

    @GetMapping("/log")
    public ResponseEntity<LogListDto> getLog() {
        List<LogEntryDto> logEntries = bankQueryService.getAuditLog().stream()
                .map(log -> {
                    LogEntryDto dto = new LogEntryDto();
                    dto.setType(log.getType().name().toLowerCase());
                    dto.setWallet_id(log.getWalletId());
                    dto.setStock_name(log.getStockName());
                    return dto;
                })
                .toList();

        LogListDto response = new LogListDto();
        response.setLog(logEntries);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chaos")
    public void chaos() {
        System.exit(1);
    }
}