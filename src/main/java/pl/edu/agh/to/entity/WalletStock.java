package pl.edu.agh.to.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"walletId", "stockName"})
})
public class WalletStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String walletId;
    private String stockName;
    private int quantity;

    public WalletStock(String walletId, String stockName, int quantity) {
        this.walletId = walletId;
        this.stockName = stockName;
        this.quantity = quantity;
    }
}
