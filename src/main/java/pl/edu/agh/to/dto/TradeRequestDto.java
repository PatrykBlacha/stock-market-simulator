package pl.edu.agh.to.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pl.edu.agh.to.entity.OperationType;

@Data
public class TradeRequestDto {

    @NotNull(message = "Pole 'type' jest wymagane (buy lub sell)")
    private OperationType type;
}
