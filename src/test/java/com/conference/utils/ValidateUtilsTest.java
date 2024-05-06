package com.conference.utils;

import com.conference.exception.InputValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class ValidateUtilsTest {

    @Test
    public void test_valid_start_and_end_times() throws InputValidationException {
        // Given
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(10, 0);

        // When
        ValidationUtils.startAndEndTimeValidation(startTime, endTime);

        // Then
        // No exception should be thrown
    }
    @Test
    public void test_start_time_equal_to_end_time() {
        // Given
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(9, 0);

        // When
        try {
            ValidationUtils.startAndEndTimeValidation(startTime, endTime);
            fail("Expected InputValidationException to be thrown");
        } catch (InputValidationException e) {
            // Then
            assertEquals("Start time cannot be equal or after end time", e.getMessage());
        }
    }

    @Test
    public void test_start_time_after_end_time() {
        // Given
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(9, 0);

        // When
        try {
            ValidationUtils.startAndEndTimeValidation(startTime, endTime);
            fail("Expected InputValidationException to be thrown");
        } catch (InputValidationException e) {
            // Then
            assertEquals("Start time cannot be equal or after end time", e.getMessage());
        }
    }

    @Test
    public void test_start_not_in_15_minute_intervals() {
        // Given
        LocalTime startTime = LocalTime.of(9, 10);
        LocalTime endTime = LocalTime.of(9, 45);

        // When
        try {
            ValidationUtils.startAndEndTimeValidation(startTime, endTime);
            fail("Expected InputValidationException to be thrown");
        } catch (InputValidationException e) {
            // Then
            assertEquals("Start or end time should be in interval of 15 minutes", e.getMessage());
        }
    }

    @Test
    public void end_start_not_in_15_minute_intervals() {
        // Given
        LocalTime startTime = LocalTime.of(9, 00);
        LocalTime endTime = LocalTime.of(9, 50);

        // When
        try {
            ValidationUtils.startAndEndTimeValidation(startTime, endTime);
            fail("Expected InputValidationException to be thrown");
        } catch (InputValidationException e) {
            // Then
            assertEquals("Start or end time should be in interval of 15 minutes", e.getMessage());
        }
    }

}
