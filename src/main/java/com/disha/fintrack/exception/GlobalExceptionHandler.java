// package com.disha.fintrack.exception;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;

// import java.time.LocalDateTime;
// import java.util.HashMap;
// import java.util.Map;

// import com.disha.fintrack.common.ApiError;
// import com.disha.fintrack.common.ErrorResponse;

// @RestControllerAdvice
// public class GlobalExceptionHandler {

// @ExceptionHandler(RuntimeException.class)
// public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
// return buildError(
// "INTERNAL_ERROR",
// ex.getMessage(),
// null,
// HttpStatus.INTERNAL_SERVER_ERROR);
// }

// @ExceptionHandler(MethodArgumentNotValidException.class)
// public ResponseEntity<ErrorResponse>
// handleValidation(MethodArgumentNotValidException ex) {
// Map<String, Object> errors = new HashMap<>();

// ex.getBindingResult().getFieldErrors()
// .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

// return buildError(
// "VALIDATION_ERROR",
// "Validation failed",
// errors,
// HttpStatus.BAD_REQUEST);
// }

// @ExceptionHandler(MethodArgumentNotValidException.class)
// public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex)
// {

// Map<String, Object> errors = new HashMap<>();

// ex.getBindingResult().getFieldErrors()
// .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

// return buildError("BAD_REQUEST", ex.getMessage(), HttpStatus.BAD_REQUEST);
// }

// @ExceptionHandler(ResourceNotFoundException.class)
// public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException
// ex) {
// return buildError("NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND);
// }

// @ExceptionHandler(Exception.class)
// public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
// return buildError("INTERNAL_ERROR", "Something went wrong",
// HttpStatus.INTERNAL_SERVER_ERROR);
// }

// private ResponseEntity<ErrorResponse> buildError(
// String code,
// String message,
// Map<String, Object> details,
// HttpStatus status) {
// ApiError error = ApiError.builder()
// .code(code)
// .message(message)
// .details(details)
// .timestamp(LocalDateTime.now())
// .build();

// return ResponseEntity
// .status(status)
// .body(new ErrorResponse(false, error));
// }
// }
