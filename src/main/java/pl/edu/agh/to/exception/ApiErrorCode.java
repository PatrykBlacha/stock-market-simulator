package pl.edu.agh.to.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiErrorCode {

    //404
    STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "Stock does not exist in the bank"),
    WALLET_NOT_FOUND(HttpStatus.NOT_FOUND, "Wallet does not exist"),

    //400
    INSUFFICIENT_BANK_STOCK(HttpStatus.BAD_REQUEST, "No stock left in the bank to buy"),
    INSUFFICIENT_WALLET_STOCK(HttpStatus.BAD_REQUEST, "No stock in the wallet to sell");

    private final HttpStatus httpStatus;
    private final String message;

    ApiErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}