package pl.edu.agh.to.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.edu.agh.to.dto.ErrorResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDto> handleApiException(ApiException ex) {
        ErrorResponseDto response = new ErrorResponseDto(
                ex.getErrorCode().name(),
                ex.getMessage()
        );

        return new ResponseEntity<>(response, ex.getErrorCode().getHttpStatus());
    }
}