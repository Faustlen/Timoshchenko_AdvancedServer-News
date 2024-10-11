package ibs.news.error;

import ibs.news.constrants.ValidationConstants;
import ibs.news.dto.response.common.CustomSuccessResponse;
import ibs.news.interceptor.LogInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler {
    private final LogInterceptor logInterceptor;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomSuccessResponse<List<ErrorCodes>>> handleValidationException(
            MethodArgumentNotValidException e) {
        List<Integer> errorCodes = e.getFieldErrors()
                .stream()
                .map(item -> ErrorCodes.getErrorCodeByMessage(item.getDefaultMessage()))
                .toList();

        List<String> errorMessages = e.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity.badRequest()
                .headers(httpHeaders -> httpHeaders.addAll(ValidationConstants.ERROR_MESSAGE, errorMessages))
                .body(new CustomSuccessResponse<>(errorCodes.getFirst(), errorCodes));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomSuccessResponse<List<ErrorCodes>>> handleElementException(
            ConstraintViolationException e) {
        List<Integer> errorCodes = e.getConstraintViolations()
                .stream()
                .map(violation -> ErrorCodes.getErrorCodeByMessage(violation.getMessage()))
                .toList();

        List<String> errorMessages = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        return ResponseEntity.badRequest()
                .headers(httpHeaders -> httpHeaders.addAll(ValidationConstants.ERROR_MESSAGE, errorMessages))
                .body(new CustomSuccessResponse<>(errorCodes.getFirst(), errorCodes));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomSuccessResponse<List<ErrorCodes>>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        ArrayList<Integer> errorCodes = new ArrayList<>();
        errorCodes.add(ErrorCodes.getErrorCodeByMessage(ValidationConstants.HTTP_MESSAGE_NOT_READABLE_EXCEPTION));

        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(ValidationConstants.HTTP_MESSAGE_NOT_READABLE_EXCEPTION);

        return ResponseEntity.badRequest()
                .headers(httpHeaders -> httpHeaders.addAll(ValidationConstants.ERROR_MESSAGE, errorMessages))
                .body(new CustomSuccessResponse<>(errorCodes.getFirst(), errorCodes));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<CustomSuccessResponse<List<ErrorCodes>>> handleIoException(HttpServletRequest request) {
        ArrayList<Integer> errorCodes = new ArrayList<>();
        errorCodes.add(ErrorCodes.getErrorCodeByMessage(ValidationConstants.UNKNOWN));

        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(ValidationConstants.UNKNOWN);

        return ResponseEntity.badRequest()
                .headers(httpHeaders -> httpHeaders.addAll(ValidationConstants.ERROR_MESSAGE, errorMessages))
                .body(new CustomSuccessResponse<>(errorCodes.getFirst(), errorCodes));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomSuccessResponse<List<ErrorCodes>>> customSuccessResponseResponseEntity(
            CustomException e) {
        ArrayList<Integer> errorCodes = new ArrayList<>();
        errorCodes.add(e.getErrorCodes().getErrorCode());

        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(e.getErrorCodes().getErrorMessage());

        return ResponseEntity.badRequest()
                .headers(httpHeaders -> httpHeaders.addAll(ValidationConstants.ERROR_MESSAGE, errorMessages))
                .body(new CustomSuccessResponse<>(errorCodes.getFirst(), errorCodes));
    }
}
