package com.conference.service;

import com.conference.dto.ConferenceRoomAvailabilityDTO;
import com.conference.dto.ConferenceRoomDTO;
import com.conference.dto.ReservationDTO;
import com.conference.exception.InputValidationException;
import com.conference.service.impl.AvailabilityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.conference.constant.ErrorCodes.START_EQ_END;
import static com.conference.constant.ErrorCodes.START_OR_END_15_MIN_INTERVAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceTest {

  @InjectMocks AvailabilityServiceImpl availabilityService;

  @Mock ReservationService reservationService;

  @Mock ConferenceRoomService conferenceRoomService;


  @Test
  public void test_start_time_equal_end_times() {
    // Given
    LocalTime startTime = LocalTime.of(9, 0);
    LocalTime endTime = LocalTime.of(9, 0);

    // When
    InputValidationException e = assertThrows(InputValidationException.class, () -> availabilityService.getConferenceRoomsAvailability(startTime, endTime, 1));

    //then
    assertEquals(START_EQ_END.getErrorCode(),e.getErrorCode());
    assertEquals(START_EQ_END.getErrorMessage(),e.getMessage());
  }
  @Test
  public void test_start_time_after_end_time() {
    // Given
    LocalTime startTime = LocalTime.of(10, 0);
    LocalTime endTime = LocalTime.of(9, 0);

    // When
    InputValidationException e = assertThrows(InputValidationException.class, () -> availabilityService.getConferenceRoomsAvailability(startTime, endTime, 1));

    // Then
    assertEquals(START_EQ_END.getErrorCode(),e.getErrorCode());
    assertEquals(START_EQ_END.getErrorMessage(),e.getMessage());
  }

  @Test
  public void test_start_time_not_in_15_minute_intervals() {
    // Given
    LocalTime startTime = LocalTime.of(9, 10);
    LocalTime endTime = LocalTime.of(9, 45);

    // When
    InputValidationException e = assertThrows(InputValidationException.class, () -> availabilityService.getConferenceRoomsAvailability(startTime, endTime, 1));

    // Then
    assertEquals(START_OR_END_15_MIN_INTERVAL.getErrorCode(),e.getErrorCode());
    assertEquals(START_OR_END_15_MIN_INTERVAL.getErrorMessage(),e.getMessage());
  }

  @Test
  public void test_end_time_not_in_15_minute_intervals() {
    // Given
    LocalTime startTime = LocalTime.of(9, 10);
    LocalTime endTime = LocalTime.of(9, 45);

    // When
    InputValidationException e = assertThrows(InputValidationException.class, () -> availabilityService.getConferenceRoomsAvailability(startTime, endTime, 1));

    // Then
    assertEquals(START_OR_END_15_MIN_INTERVAL.getErrorCode(),e.getErrorCode());
    assertEquals(START_OR_END_15_MIN_INTERVAL.getErrorMessage(),e.getMessage());
  }



  @Test
  public void test_list_of_conference_rooms_with_availability() {
    // Given
    LocalTime startTime = LocalTime.of(9, 0);
    LocalTime endTime = LocalTime.of(10, 0);
    List<Integer> reservationsList = Collections.EMPTY_LIST;

    ReservationDTO reservationDTO = new ReservationDTO(LocalDate.now(),startTime,endTime,1);

    when(reservationService.findReservationsAndMaintenance(reservationDTO))
        .thenReturn(reservationsList);
    when(conferenceRoomService.getConferenceRoomsByLocation(1)).thenReturn(getRoomList());

    // When
    List<ConferenceRoomAvailabilityDTO> result =
        availabilityService.getConferenceRoomsAvailability(startTime, endTime, 1);

    assertEquals(2, result.size());
    assertEquals("Room 1", result.get(0).roomName());
    assertEquals(10, result.get(0).roomCapacity());
    assertTrue(result.get(0).available());
  }

  @Test
  public void test_first_room_avaialble_second_not_availability() {
    // Given
    LocalTime startTime = LocalTime.of(9, 0);
    LocalTime endTime = LocalTime.of(10, 0);
    List<Integer> reservationsList = Arrays.asList(2);

    ReservationDTO reservationDTO = new ReservationDTO(LocalDate.now(),startTime,endTime,1);

    when(reservationService.findReservationsAndMaintenance(reservationDTO))
            .thenReturn(reservationsList);
    when(conferenceRoomService.getConferenceRoomsByLocation(1)).thenReturn(getRoomList());

    // When
    List<ConferenceRoomAvailabilityDTO> result =
            availabilityService.getConferenceRoomsAvailability(startTime, endTime, 1);

    assertEquals(2, result.size());
    assertEquals("Room 1", result.get(0).roomName());
    assertEquals(10, result.get(0).roomCapacity());
    assertTrue(result.get(0).available());
    assertEquals("Room 2", result.get(1).roomName());
    assertFalse(result.get(1).available());
  }

  @Test
  public void test_no_available_conference_rooms() {
    // Given
    LocalTime startTime = LocalTime.of(9, 0);
    LocalTime endTime = LocalTime.of(10, 0);
    List<Integer> reservationsList = Arrays.asList(1,2);

    ReservationDTO reservationDTO = new ReservationDTO(LocalDate.now(),startTime,endTime,1);

    when(reservationService.findReservationsAndMaintenance(reservationDTO))
        .thenReturn(reservationsList);
    when(conferenceRoomService.getConferenceRoomsByLocation(1)).thenReturn(getRoomList());

    // When
    List<ConferenceRoomAvailabilityDTO> result =
        availabilityService.getConferenceRoomsAvailability(startTime, endTime, 1);

    assertEquals(2, result.size());
    assertEquals("Room 1", result.get(0).roomName());
    assertEquals(10, result.get(0).roomCapacity());
    assertFalse(result.get(0).available());
    assertEquals("Room 2", result.get(1).roomName());
    assertEquals(5, result.get(1).roomCapacity());
    assertFalse(result.get(0).available());
  }



  private List<ConferenceRoomDTO> getRoomList() {
    List<ConferenceRoomDTO> roomList = new ArrayList<>();
    ConferenceRoomDTO room = new ConferenceRoomDTO(1,"Room 1",10,1);
    ConferenceRoomDTO room2 = new ConferenceRoomDTO(2,"Room 2",5,1);
    roomList.add(room);
    roomList.add(room2);
    return roomList;
  }
}
