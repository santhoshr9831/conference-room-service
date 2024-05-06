package com.conference.exception;

import com.conference.controller.request.Response;
import com.conference.utils.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static com.conference.utils.Constants.ARGUMENT_TYPE_MISMATCH;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Response> methodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    return ResponseEntity.badRequest()
        .body(
            new Response(
                exception.getBindingResult().getFieldError().getDefaultMessage(),
                ErrorCodes.INPUT_VALIDATION_FAILURE));
  }

  @ExceptionHandler(InputValidationException.class)
  public ResponseEntity<Response> inputValidation(Exception e) {
    return ResponseEntity.badRequest()
        .body(new Response(e.getMessage(), ErrorCodes.INPUT_VALIDATION_FAILURE));
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Response> parameterValidation(MethodArgumentTypeMismatchException e) {
    return ResponseEntity.badRequest()
        .body(
            new Response(
                String.format(ARGUMENT_TYPE_MISMATCH, e.getName()),
                ErrorCodes.INPUT_VALIDATION_FAILURE));
  }

  @ExceptionHandler(ConferenceRoomNotAvailableException.class)
  public ResponseEntity<Response> conferenceRoomNotAvailable(
      ConferenceRoomNotAvailableException e) {
    return ResponseEntity.badRequest()
        .body(new Response(e.getMessage(), ErrorCodes.CONFERENCE_ROOM_NOT_AVAILABLE));
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<Response> resourceNotFoundException(NoResourceFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new Response(e.getMessage(), ErrorCodes.RESOURCE_NOT_FOUND_EXCEPTION));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Response> generalException(Exception e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new Response(e.getMessage(), ErrorCodes.GENERIC_EXCEPTION));
  }
}
