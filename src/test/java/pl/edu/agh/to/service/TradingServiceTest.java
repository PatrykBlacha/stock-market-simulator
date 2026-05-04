package pl.edu.agh.to.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.agh.to.entity.BankStock;
import pl.edu.agh.to.exception.ApiException;
import pl.edu.agh.to.repository.BankStockRepository;
import pl.edu.agh.to.repository.WalletStockRepository;
import pl.edu.agh.to.service.TradingService;
import pl.edu.agh.to.entity.OperationType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TradingServiceRaceConditionTest {

    @Autowired
    private TradingService tradingService;

    @Autowired
    private BankStockRepository bankStockRepo;

    @Autowired
    private WalletStockRepository walletStockRepo;

    @BeforeEach
    void setup() {
        // Czyścimy bazę przed testem
        walletStockRepo.deleteAll();
        bankStockRepo.deleteAll();

        // Dodajemy TYLKO 1 akcję do banku
        bankStockRepo.save(new BankStock("AAPL", 1));
    }

    @Test
    void shouldNotAllowToBuyMoreStocksThanAvailableInBank() throws InterruptedException {
        // Symulujemy 10 jednoczesnych requestów (10 wątków)
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successfulBuys = new AtomicInteger(0);
        AtomicInteger failedBuys = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    // Wszyscy próbują kupić AAPL w tym samym momencie
                    tradingService.processTrade("wallet-1", "AAPL", OperationType.BUY);
                    successfulBuys.incrementAndGet();
                } catch (ApiException e) {
                    // Oczekujemy, że 9 na 10 requestów rzuci błąd (brak akcji)
                    failedBuys.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        // Czekamy, aż wszystkie 10 wątków skończy pracę
        latch.await();

        // W banku powinno być 0 akcji (nigdy poniżej zera!)
        BankStock stockAfterTest = bankStockRepo.findById("AAPL").orElseThrow();
        assertEquals(0, stockAfterTest.getQuantity());

        // Tylko 1 transakcja powinna zakończyć się sukcesem
        assertEquals(1, successfulBuys.get());

        // Pozostałe 9 powinno dostać błąd
        assertEquals(9, failedBuys.get());
    }
}