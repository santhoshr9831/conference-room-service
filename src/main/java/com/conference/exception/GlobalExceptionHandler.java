package com.conference.exception;

import com.conference.controller.request.Response;
import com.conference.utils.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .body(new Response<>(exception.getBindingResult().getFieldError().getDefaultMessage(),
                        ErrorCodes.INPUT_VALIDATION_FAILURE));
    }

    @ExceptionHandler(InputValidationException.class)
    public ResponseEntity<Response> inputValidation(InputValidationException e){
        return ResponseEntity.badRequest()
                .body(new Response(e.getMessage(),ErrorCodes.INPUT_VALIDATION_FAILURE));
    }

    @ExceptionHandler(ConferenceRoomNotAvailableException.class)
    public ResponseEntity<Response> ConferenceRoomNotAvailable(ConferenceRoomNotAvailableException e){
        return ResponseEntity.badRequest()
                .body(new Response(e.getMessage(),ErrorCodes.CONFERENCE_ROOM_NOT_AVAILABLE));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> ResourceNotFoundException(NoResourceFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(e.getMessage(), ErrorCodes.RESOURCE_NOT_FOUND_EXCEPTION));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> generalException(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response<>(e.getMessage(), ErrorCodes.GENERIC_EXCEPTION));
    }
}
