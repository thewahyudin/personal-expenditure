package why.din.personal.expenditure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import why.din.personal.expenditure.dto.BadRequestResponse;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BadRequestResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    BindingResult bindingResult = ex.getBindingResult();

    List<BadRequestResponse.FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
            .map(error -> new BadRequestResponse.FieldError(error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());

    BadRequestResponse errorResponse = new BadRequestResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid input",
            fieldErrors
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}