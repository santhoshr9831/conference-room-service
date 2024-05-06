package com.conference.controller;

import com.conference.controller.api.ReservationController;
import com.conference.controller.request.ReservationRequest;
import com.conference.controller.request.ReservationResponse;
import com.conference.exception.ConferenceRoomNotAvailableException;
import com.conference.exception.InputValidationException;
import com.conference.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {

  @InjectMocks ReservationController controller;
  @Mock ReservationService reservationService;

  public ReservationResponse getReservationResponse() {
    return ReservationResponse.builder()
        .reservationId(1)
        .roomId(1)
        .roomName("Room1")
        .roomCapacity(4)
        .locationId(1)
        .meetingDate(LocalDate.now())
        .startTime(LocalTime.now())
        .endTime(LocalTime.now().plusHours(1))
        .build();
  }

  @Test
  public void test_valid_reservation_request() throws Exception {
    // Given
    ReservationRequest request = new ReservationRequest();
    request.setNoOfParticipants(2);
    LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 15));
    LocalDateTime endTime = startTime.plusHours(1);
    request.setStartTime(startTime);
    request.setEndTime(endTime);
    when(reservationService.reserveConferenceRoom(any())).thenReturn(getReservationResponse());

    // When
    ResponseEntity<ReservationResponse> response = controller.reserveRoom(request);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(ReservationResponse.class, response.getBody().getClass());
    assertEquals("Room1", response.getBody().getRoomName());
  }

  @Test
  public void test_no_of_participants_as_1() {
    // Given
    ReservationRequest request = new ReservationRequest();
    request.setNoOfParticipants(1);
    LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 15));
    LocalDateTime endTime = startTime.plusHours(1);
    request.setStartTime(startTime);
    request.setEndTime(endTime);

    // When & Then
    assertThrows(InputValidationException.class, () -> controller.reserveRoom(request));
  }

  @Test
  public void test_no_of_participants_2() throws ConferenceRoomNotAvailableException, InputValidationException {
    // Given
    ReservationRequest request = new ReservationRequest();
    request.setNoOfParticipants(2);
    LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 15));
    LocalDateTime endTime = startTime.plusHours(1);
    request.setStartTime(startTime);
    request.setEndTime(endTime);
    when(reservationService.reserveConferenceRoom(any())).thenReturn(getReservationResponse());

    // When
    ResponseEntity<ReservationResponse> response = controller.reserveRoom(request);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(ReservationResponse.class, response.getBody().getClass());
  }

  @Test
  public void test_reservation_NotOnCurrentDate() throws ConferenceRoomNotAvailableException {
    // Given
    ReservationRequest request = new ReservationRequest();
    request.setNoOfParticipants(5);
    request.setStartTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 0)));
    request.setEndTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(12, 0)));
    request.setLocationId(1);

    // When
    InputValidationException exception = assertThrows(InputValidationException.class, () -> {
      controller.reserveRoom(request);
    });

    // Then
    assertEquals("Reservation allowed only for current date", exception.getMessage());
  }

  @Test
  public void test_input_validation_exception_different_dates() {
    // Given
    ReservationRequest request = new ReservationRequest();
    request.setNoOfParticipants(5);
    request.setStartTime(LocalDateTime.of(2022, 1, 1, 10, 0));
    request.setEndTime(LocalDateTime.of(2022, 1, 2, 10, 0));

    // When
    InputValidationException exception = assertThrows(InputValidationException.class, () -> {
      controller.reserveRoom(request);
    });

    // Then
    assertEquals("Reservation allowed only for current date", exception.getMessage());
  }

  @Test
  public void test_conference_room_not_available_exception() throws ConferenceRoomNotAvailableException {
    // Given
    ReservationRequest request = new ReservationRequest();
    request.setNoOfParticipants(5);
    LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 15));
    LocalDateTime endTime = startTime.plusHours(1);
    request.setStartTime(startTime);
    request.setEndTime(endTime);
    request.setLocationId(1);
    when(reservationService.reserveConferenceRoom(any())).thenThrow(new ConferenceRoomNotAvailableException("Conference room not available with expected capacity"));


    // When
    assertThrows(ConferenceRoomNotAvailableException.class, () -> {controller.reserveRoom(request);});

  }
}
