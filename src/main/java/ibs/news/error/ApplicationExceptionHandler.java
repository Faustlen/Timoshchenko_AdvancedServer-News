package ibs.news.error;

import ibs.news.constrants.ValidationConstants;
import ibs.news.dto.response.common.CustomSuccessResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomSuccessResponse<?>> handleValidationException(
            MethodArgumentNotValidException e) {

        List<Integer> errorCodes = e.getFieldErrors().stream().map(item ->
                ErrorCodes.getErrorCodeByMessage(item.getDefaultMessage())).toList();

        return new ResponseEntity<>(
                new CustomSuccessResponse<>(errorCodes.getFirst(), errorCodes), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public CustomSuccessResponse<?> handleElementException(ConstraintViolationException e) {
        List<Integer> errorCodes = e.getConstraintViolations().stream()
                .map(violation -> ErrorCodes.getErrorCodeByMessage(violation.getMessage())).toList();

        return new CustomSuccessResponse<>(errorCodes.getFirst(), errorCodes);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public CustomSuccessResponse<?> handleNotFoundError(NoResourceFoundException e) {
        CustomSuccessResponse<?> response = new CustomSuccessResponse<>(404, List.of(404));
        response.setSuccess(false);
        response.setError("Not Found");

        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CustomSuccessResponse<?> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {

        var errorCodes = new ArrayList<Integer>();
        errorCodes.add(ErrorCodes.getErrorCodeByMessage(ValidationConstants.HTTP_MESSAGE_NOT_READABLE_EXCEPTION));

        return new CustomSuccessResponse<>(errorCodes.getFirst(), errorCodes);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public CustomSuccessResponse<?> handleMissingParams(MissingServletRequestParameterException e) {

        var errorCodes = new ArrayList<Integer>();
        errorCodes.add(ErrorCodes.getErrorCodeByMessage(ValidationConstants.UNKNOWN));

        return new CustomSuccessResponse<>(errorCodes.getFirst(), errorCodes);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomSuccessResponse<?>> customSuccessResponseResponseEntity(CustomException e) {

        var errorCodes = new ArrayList<Integer>();
        errorCodes.add(e.getErrorCodes().getErrorCode());

        return new ResponseEntity<>(new CustomSuccessResponse<>(errorCodes.getFirst(), errorCodes), e.getHttpStatus());
    }
}
