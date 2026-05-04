package pl.edu.agh.to.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.to.entity.AuditLog;
import pl.edu.agh.to.entity.BankStock;
import pl.edu.agh.to.entity.WalletStock;
import pl.edu.agh.to.repository.AuditLogRepository;
import pl.edu.agh.to.repository.BankStockRepository;
import pl.edu.agh.to.repository.WalletStockRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankQueryService {

    private final BankStockRepository bankStockRepo;
    private final WalletStockRepository walletStockRepo;
    private final AuditLogRepository auditLogRepo;

    public List<BankStock> getBankState() {
        return bankStockRepo.findAll();
    }

    @Transactional
    public void setBankState(List<BankStock> newStocks) {
        bankStockRepo.deleteAll();
        bankStockRepo.saveAll(newStocks);
    }

    public List<WalletStock> getWalletState(String walletId) {
        return walletStockRepo.findAllByWalletId(walletId);
    }

    public int getWalletStockQuantity(String walletId, String stockName) {
        return walletStockRepo.findByWalletIdAndStockName(walletId, stockName)
                .map(WalletStock::getQuantity)
                .orElse(0);
    }

    public List<AuditLog> getAuditLog() {
        return auditLogRepo.findTop10000ByOrderByIdAsc();
    }
}