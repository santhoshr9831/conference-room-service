package com.conference.exception;

import lombok.Getter;

import java.util.StringJoiner;

@Getter
public class InputValidationException extends Exception {
  private final String errorCode;
  public InputValidationException(String errorCode,String message) {
    super(message);
    this.errorCode = errorCode;
  }
  @Override
  public String toString() {
    return new StringJoiner(" ")
            .add("InputValidationException")
            .add("errorCode =")
            .add(errorCode)
            .add("message =")
            .add(getMessage())
            .toString();
  }
}
