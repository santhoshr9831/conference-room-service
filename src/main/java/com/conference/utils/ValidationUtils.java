package com.conference.utils;

import com.conference.exception.InputValidationException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

import static com.conference.constant.ErrorCodes.START_EQ_END;
import static com.conference.constant.ErrorCodes.START_OR_END_15_MIN_INTERVAL;

@Slf4j
public class ValidationUtils {

  private ValidationUtils() {}

  public static void startAndEndTimeValidation(LocalTime startTime, LocalTime endTime)
      throws InputValidationException {
    if (startTime.equals(endTime) || startTime.isAfter(endTime)) {
      log.info(START_EQ_END.getErrorMessage());
      throw new InputValidationException(START_EQ_END.getErrorCode(),START_EQ_END.getErrorMessage());
    }
    if (startTime.getMinute() % 15 != 0 || endTime.getMinute() % 15 != 0) {
      log.info(START_OR_END_15_MIN_INTERVAL.getErrorMessage());
      throw new InputValidationException(START_OR_END_15_MIN_INTERVAL.getErrorCode(),START_OR_END_15_MIN_INTERVAL.getErrorMessage());
    }
  }
}
