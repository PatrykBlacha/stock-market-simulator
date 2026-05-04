package pl.edu.agh.to.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class WalletStateDto {
    private String id;
    private List<StockDto> stocks;
}