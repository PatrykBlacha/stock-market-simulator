package pl.edu.agh.to.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OperationType {
    @JsonProperty("buy")
    BUY,

    @JsonProperty("sell")
    SELL
}
