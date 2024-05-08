package com.conference.exception;

import com.conference.dto.response.ErrorResponse;
import com.conference.constant.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static com.conference.constant.ErrorCodes.*;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> methodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    return ResponseEntity.badRequest()
        .body(
            new ErrorResponse(
                exception.getBindingResult().getFieldError().getDefaultMessage(),
                ErrorCodes.INPUT_VALIDATION_FAILURE.getErrorCode()));
  }

  @ExceptionHandler(InputValidationException.class)
  public ResponseEntity<ErrorResponse> inputValidation(InputValidationException e) {
    return ResponseEntity.badRequest()
        .body(new ErrorResponse(e.getMessage(), e.getErrorCode()));
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
  public ResponseEntity<ErrorResponse> parameterValidation(RuntimeException e) {
    return ResponseEntity.badRequest()
        .body(
            new ErrorResponse(INPUT_VALIDATION_FAILURE.getErrorMessage(),
                ErrorCodes.INPUT_VALIDATION_FAILURE.getErrorCode()));
  }

  @ExceptionHandler(ConferenceRoomNotAvailableException.class)
  public ResponseEntity<ErrorResponse> conferenceRoomNotAvailable(
      ConferenceRoomNotAvailableException e) {
    return ResponseEntity.badRequest()
        .body(new ErrorResponse(e.getMessage(), e.getErrorCode()));
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> resourceNotFoundException(NoResourceFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(e.getMessage(), ErrorCodes.RESOURCE_NOT_FOUND_EXCEPTION.getErrorCode()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> generalException(Exception e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e.getMessage(), GENERIC_EXCEPTION.getErrorCode()));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> runtimeException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            new ErrorResponse(
                    INTERNAL_SERVER_ERROR.getErrorMessage(),
                    INTERNAL_SERVER_ERROR.getErrorCode()));
  }
}
