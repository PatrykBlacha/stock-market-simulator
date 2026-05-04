package pl.edu.agh.to.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.to.entity.AuditLog;
import pl.edu.agh.to.entity.BankStock;
import pl.edu.agh.to.entity.OperationType;
import pl.edu.agh.to.entity.WalletStock;
import pl.edu.agh.to.exception.ApiErrorCode;
import pl.edu.agh.to.exception.ApiException;
import pl.edu.agh.to.repository.AuditLogRepository;
import pl.edu.agh.to.repository.BankStockRepository;
import pl.edu.agh.to.repository.WalletStockRepository;

@Service
@RequiredArgsConstructor
public class TradingService {

    private final BankStockRepository bankStockRepo;
    private final WalletStockRepository walletStockRepo;
    private final AuditLogRepository auditLogRepo;

    @Transactional
    public void processTrade(String walletId, String stockName, OperationType type) {

        BankStock bankStock = bankStockRepo.findByNameForUpdate(stockName)
                .orElseThrow(() -> new ApiException(ApiErrorCode.STOCK_NOT_FOUND, "Stock not found in the bank"));

        if (type == OperationType.BUY) {
            handleBuy(walletId, bankStock);
        } else if (type == OperationType.SELL) {
            handleSell(walletId, bankStock);
        }
    }

    private void handleBuy(String walletId, BankStock bankStock) {
        if (bankStock.getQuantity() <= 0) {
            throw new ApiException(ApiErrorCode.INSUFFICIENT_BANK_STOCK, "No stock left in the bank to buy");
        }

        bankStock.setQuantity(bankStock.getQuantity() - 1);
        bankStockRepo.save(bankStock);

        WalletStock walletStock = walletStockRepo.findByWalletIdAndStockName(walletId, bankStock.getName())
                .orElse(new WalletStock(walletId, bankStock.getName(), 0));

        walletStock.setQuantity(walletStock.getQuantity() + 1);
        walletStockRepo.save(walletStock);

        auditLogRepo.save(new AuditLog(OperationType.BUY, walletId, bankStock.getName()));
    }

    private void handleSell(String walletId, BankStock bankStock) {

        WalletStock walletStock = walletStockRepo.findByWalletIdAndStockName(walletId, bankStock.getName())
                .orElseThrow(() -> new ApiException(ApiErrorCode.WALLET_NOT_FOUND, "Wallet does not exist"));

        if (walletStock.getQuantity() <= 0) {
            throw new ApiException(ApiErrorCode.INSUFFICIENT_WALLET_STOCK, "No stock in the wallet to sell");
        }

        walletStock.setQuantity(walletStock.getQuantity() - 1);
        walletStockRepo.save(walletStock);

        bankStock.setQuantity(bankStock.getQuantity() + 1);
        bankStockRepo.save(bankStock);

        auditLogRepo.save(new AuditLog(OperationType.SELL, walletId, bankStock.getName()));
    }
}