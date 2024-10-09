package ibs.news.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCodes errorCodes;

    private final HttpStatus httpStatus;

    public CustomException(ErrorCodes errorCodes, HttpStatus httpStatus) {
        this.errorCodes = errorCodes;

        this.httpStatus = httpStatus;
    }
}
