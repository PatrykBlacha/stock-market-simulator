package pl.edu.agh.to.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.to.entity.AuditLog;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findTop10000ByOrderByIdAsc();
}