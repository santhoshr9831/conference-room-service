package com.conference.service;

import com.conference.dto.ConferenceRoomDTO;
import com.conference.dto.ReservationDTO;
import com.conference.dto.response.ReservationResponse;
import com.conference.entity.Reservation;
import com.conference.exception.ConferenceRoomNotAvailableException;
import com.conference.exception.InputValidationException;
import com.conference.repository.ReservationRepository;
import com.conference.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.conference.constant.ErrorCodes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
  @InjectMocks ReservationServiceImpl reservationService;
  @Mock ConferenceRoomService roomService;
  @Mock ReservationRepository reservationRepository;
  @Mock ModelMapper modelMapper;


  @Test
  public void test_reserve_conference_room_with_minimum_participants() {
    // Given
    LocalDate meetingDate = LocalDate.now();
    LocalTime startTime = LocalTime.of(9, 0);
    LocalTime endTime = LocalTime.of(10, 0);

    ReservationDTO reservationDTO = new ReservationDTO(null,null,1,meetingDate,startTime,endTime,1);

    // When/Then
    InputValidationException e = assertThrows(InputValidationException.class, () -> reservationService.reserveConferenceRoom(reservationDTO));
    assertEquals(MIN_PARTICIPANT_NOT_MET.getErrorCode(),e.getErrorCode());
    assertEquals(MIN_PARTICIPANT_NOT_MET.getErrorMessage(),e.getMessage());
  }

  @Test
  public void test_reserve_conference_room_with_15_minute_intervals() {
    // Given
    LocalDate meetingDate = LocalDate.now();
    LocalTime startTime = LocalTime.of(9, 10);
    LocalTime endTime = LocalTime.of(10, 0);

    ReservationDTO reservationDTO = new ReservationDTO(null,null,10,meetingDate,startTime,endTime,1);

    // When/Then
    InputValidationException e= assertThrows(InputValidationException.class, () -> reservationService.reserveConferenceRoom(reservationDTO));
    assertEquals(START_OR_END_15_MIN_INTERVAL.getErrorCode(),e.getErrorCode());
    assertEquals(START_OR_END_15_MIN_INTERVAL.getErrorMessage(),e.getMessage());
  }

  @Test
  public void test_reserve_conference_room_with_start_time_equal_to_end_time() {
    // Given
    LocalDate meetingDate = LocalDate.now();
    LocalTime startTime = LocalTime.of(9, 0);
    LocalTime endTime = LocalTime.of(9, 0);

    ReservationDTO reservationDTO = new ReservationDTO(null,null,10,meetingDate,startTime,endTime,1);

    // When/Then
    InputValidationException e= assertThrows(InputValidationException.class, () -> reservationService.reserveConferenceRoom(reservationDTO));
    assertEquals(START_EQ_END.getErrorCode(),e.getErrorCode());
    assertEquals(START_EQ_END.getErrorMessage(),e.getMessage());
  }
  @Test
  public void test_reserve_conference_room_success() throws ConferenceRoomNotAvailableException {
    // Given
    LocalDate meetingDate = LocalDate.now();
    LocalTime startTime = LocalTime.of(9, 0);
    LocalTime endTime = LocalTime.of(10, 0);
    ReservationDTO reservationDTO = getReservationDTO();
    Reservation reservation = new Reservation();
    reservation.setRoomId(1);
    reservation.setReservationId(1);
    reservation.setMeetingDate(meetingDate);
    reservation.setStartTime(startTime);
    reservation.setEndTime(endTime);


    List<Integer> reservationList = new ArrayList<>();

    when(roomService.getConferenceRoomsMatchingCapacity(
            reservationDTO.roomCapacity(), reservationDTO.locationId()))
        .thenReturn(getRoomList());
    when(reservationRepository.findReservationsAndMaintenance(
            reservationDTO.meetingDate(), reservationDTO.startTime(), reservationDTO.endTime(),reservationDTO.locationId()))
        .thenReturn(reservationList);
    when(reservationRepository.save(any())).thenReturn(reservation);

    // When
    ReservationResponse response = reservationService.reserveConferenceRoom(reservationDTO);

    // Then
    assertNotNull(response);
    assertEquals(1, response.reservationId());
    assertEquals("Room 1", response.roomName());
    assertEquals(1, response.roomId());
    assertEquals(10, response.roomCapacity());
    assertEquals(1, response.locationId());
    assertTrue(meetingDate.toString().equals(response.meetingDate()) );
    assertEquals(startTime.toString(), response.startTime());
    assertEquals(endTime.toString(), response.endTime());
  }

  @Test
  public void test_conference_room_not_available_for_given_capacity()
      throws ConferenceRoomNotAvailableException {
    // Given
    ReservationDTO reservationDTO = getReservationDTO();

    when(roomService.getConferenceRoomsMatchingCapacity(
            reservationDTO.roomCapacity(), reservationDTO.locationId()))
        .thenReturn(Collections.emptyList());

    // When
    Exception e =
        assertThrows(
            ConferenceRoomNotAvailableException.class,
            () -> {
              reservationService.reserveConferenceRoom(reservationDTO);
            });
    assertEquals(ROOM_NOT_AVAILABLE_OR_CAPACITY_NOT_MET.getErrorMessage(), e.getMessage());
  }

  @Test
  public void test_conference_room_not_available_due_to_maintenance()
      throws ConferenceRoomNotAvailableException {
    // Given
    ReservationDTO reservationDTO = getReservationDTO();
    LocalDate meetingDate = LocalDate.now();
    LocalTime startTime = LocalTime.of(9, 0);
    LocalTime endTime = LocalTime.of(10, 0);

    when(roomService.getConferenceRoomsMatchingCapacity(
            reservationDTO.roomCapacity(), reservationDTO.locationId()))
        .thenReturn(getRoomList());

    List<Integer> reservationList = Arrays.asList(0, 1);
    when(reservationRepository.findReservationsAndMaintenance(meetingDate, startTime, endTime, 1))
        .thenReturn(reservationList);

    // When
    Exception e =
        assertThrows(
            ConferenceRoomNotAvailableException.class,
            () -> {
              reservationService.reserveConferenceRoom(reservationDTO);
            });

    assertEquals(e.getMessage(), "Reservation overlaps maintenance window");
  }

  @Test
  public void test_conference_room_already_reserved() throws ConferenceRoomNotAvailableException {
    // Given
    LocalDate meetingDate = LocalDate.now();
    LocalTime startTime = LocalTime.of(9, 0);
    LocalTime endTime = LocalTime.of(10, 0);

    ReservationDTO reservationDTO = getReservationDTO();

    Reservation reservation = new Reservation();
    reservation.setRoomId(1);
    reservation.setReservationId(1);
    reservation.setMeetingDate(meetingDate);
    reservation.setStartTime(startTime);
    reservation.setEndTime(endTime);

    when(roomService.getConferenceRoomsMatchingCapacity(reservationDTO.roomCapacity(), reservationDTO.locationId()))
        .thenReturn(getRoomList());

    List<Integer> reservationList = Arrays.asList(1);
    when(reservationRepository.findReservationsAndMaintenance(
            meetingDate,startTime,endTime, 1))
        .thenReturn(reservationList);

    // When
    Exception e =
        assertThrows(
            ConferenceRoomNotAvailableException.class,
            () -> {
              reservationService.reserveConferenceRoom(reservationDTO);
            });
    assertEquals(ALL_ROOMS_RESERVED.getErrorMessage(), e.getMessage());
  }

  private List<ConferenceRoomDTO> getRoomList() {
    List<ConferenceRoomDTO> roomList = new ArrayList<>();
    roomList.add(new ConferenceRoomDTO(1,"Room 1",10,1));
    return roomList;
  }

  private ReservationDTO getReservationDTO(){
    LocalTime startTime = LocalTime.of(9, 0);
    LocalTime endTime = LocalTime.of(10, 0);

    return new ReservationDTO(null,null,10,LocalDate.now(),startTime,endTime,1);
  }
}
