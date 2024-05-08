package com.conference.controller;

import com.conference.dto.request.ReservationRequest;
import com.conference.dto.response.ReservationResponse;
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
    return new ReservationResponse(
        1,
        1,
        "Room1",
        4,
        1,
        LocalDate.now().toString(),
        LocalTime.now().toString(),
        LocalTime.now().plusHours(1).toString());
  }

  @Test
  public void test_valid_reservation_request() throws Exception {
    // Given
    LocalTime startTime = LocalTime.of(10, 15);
    ReservationRequest request =
        new ReservationRequest(2, null, startTime, startTime.plusHours(1), null);
    when(reservationService.reserveConferenceRoom(any())).thenReturn(getReservationResponse());

    // When
    ResponseEntity<ReservationResponse> response = controller.reserveRoom(request);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(ReservationResponse.class, response.getBody().getClass());
    assertEquals("Room1", response.getBody().roomName());
  }

}
