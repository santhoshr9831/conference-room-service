package com.conference.utils;

import com.conference.exception.InputValidationException;
import java.time.LocalTime;

public class ValidationUtils {

  private ValidationUtils() {}

  public static void startAndEndTimeValidation(LocalTime startTime, LocalTime endTime)
      throws InputValidationException {
    if (startTime.equals(endTime) || startTime.isAfter(endTime)) {
      throw new InputValidationException("Start time cannot be equal or after end time");
    }
    if (startTime.getMinute() % 15 != 0 || endTime.getMinute() % 15 != 0) {
      throw new InputValidationException("Start or end time should be in interval of 15 minutes");
    }
  }
}
