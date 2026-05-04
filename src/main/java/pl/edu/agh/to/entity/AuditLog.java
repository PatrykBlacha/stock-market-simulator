package pl.edu.agh.to.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OperationType type;
    private String walletId;
    private String stockName;

    public AuditLog(OperationType type, String walletId, String stockName) {
        this.type = type;
        this.walletId = walletId;
        this.stockName = stockName;
    }
}
