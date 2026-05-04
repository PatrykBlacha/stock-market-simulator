package pl.edu.agh.to.dto;

import lombok.Data;

import java.util.List;

@Data
public class StockListDto {
    private List<StockDto> stocks;
}
