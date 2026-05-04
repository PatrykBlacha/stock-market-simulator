package pl.edu.agh.to.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.agh.to.entity.BankStock;

import java.util.Optional;

public interface BankStockRepository extends JpaRepository<BankStock, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BankStock b WHERE b.name = :name")
    Optional<BankStock> findByNameForUpdate(@Param("name") String name);
}
