package pl.edu.agh.to.dto;

import lombok.Data;

@Data
public class LogEntryDto {
    private String type;
    private String wallet_id;
    private String stock_name;
}
