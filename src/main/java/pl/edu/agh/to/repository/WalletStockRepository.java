package pl.edu.agh.to.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.to.entity.WalletStock;

import java.util.List;
import java.util.Optional;

public interface WalletStockRepository extends JpaRepository<WalletStock, Long> {
    Optional<WalletStock> findByWalletIdAndStockName(String walletId, String stockName);
    List<WalletStock> findAllByWalletId(String walletId);
}
