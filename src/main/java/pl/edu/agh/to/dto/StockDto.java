package pl.edu.agh.to.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockDto {
    private String name;
    private int quantity;
}