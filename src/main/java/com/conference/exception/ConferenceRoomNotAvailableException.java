package com.conference.exception;

import lombok.Getter;

import java.util.StringJoiner;

@Getter
public class ConferenceRoomNotAvailableException extends RuntimeException {
  private final String errorCode;

  public ConferenceRoomNotAvailableException(String errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  @Override
  public String toString() {
    return new StringJoiner(" ")
        .add("ConferenceRoomNotAvailableException")
        .add("errorCode =")
        .add(errorCode)
        .add("message =")
        .add(getMessage())
        .toString();
  }
}
