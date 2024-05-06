package com.conference.service;

import com.conference.controller.request.ReservationResponse;
import com.conference.dto.ConferenceRoomDTO;
import com.conference.dto.ReservationDTO;
import com.conference.entity.Reservation;
import com.conference.exception.ConferenceRoomNotAvailableException;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
  @InjectMocks ReservationServiceImpl reservationService;
  @Mock ConferenceRoomService roomService;
  @Mock ReservationRepository reservationRepository;
  @Mock ModelMapper modelMapper;

  @Test
  public void test_reserve_conference_room_success() throws ConferenceRoomNotAvailableException {
    // Given
    ReservationDTO reservationDTO =
        ReservationDTO.builder()
            .roomCapacity(10)
            .locationId(1)
            .meetingDate(LocalDate.now().plusDays(1))
            .startTime(LocalTime.of(9, 0))
            .endTime(LocalTime.of(10, 0))
            .build();

    Reservation reservation = new Reservation();
    reservation.setRoomId(1);
    reservation.setReservationId(1);
    reservation.setMeetingDate(LocalDate.now().plusDays(1));
    reservation.setStartTime(LocalTime.of(9, 0));
    reservation.setEndTime(LocalTime.of(10, 0));

    List<Integer> reservationList = new ArrayList<>();

    when(roomService.findConferenceRoomsMatchingCapacity(
            reservationDTO.getRoomCapacity(), reservationDTO.getLocationId()))
        .thenReturn(getRoomList());
    when(reservationRepository.findReservationsAndMaintenance(
            LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(10, 0), 1))
        .thenReturn(reservationList);
    when(modelMapper.map(reservationDTO, Reservation.class)).thenReturn(reservation);

    // When
    ReservationResponse response = reservationService.reserveConferenceRoom(reservationDTO);

    // Then
    assertNotNull(response);
    assertEquals(1, response.getReservationId());
    assertEquals("Room 1", response.getRoomName());
    assertEquals(1, response.getRoomId());
    assertEquals(10, response.getRoomCapacity());
    assertEquals(1, response.getLocationId());
    assertEquals(LocalDate.now().plusDays(1), response.getMeetingDate());
    assertEquals(LocalTime.of(9, 0), response.getStartTime());
    assertEquals(LocalTime.of(10, 0), response.getEndTime());
  }

  @Test
  public void test_conference_room_not_available_for_given_capacity()
      throws ConferenceRoomNotAvailableException {
    // Given
    ReservationDTO reservationDTO =
        ReservationDTO.builder()
            .roomCapacity(10)
            .locationId(1)
            .meetingDate(LocalDate.now().plusDays(1))
            .startTime(LocalTime.of(9, 0))
            .endTime(LocalTime.of(10, 0))
            .build();

    when(roomService.findConferenceRoomsMatchingCapacity(
            reservationDTO.getRoomCapacity(), reservationDTO.getLocationId()))
        .thenReturn(Collections.emptyList());

    // When
    Exception e =
        assertThrows(
            ConferenceRoomNotAvailableException.class,
            () -> {
              reservationService.reserveConferenceRoom(reservationDTO);
            });
    assertEquals(e.getMessage(), "Conference room not available with expected capacity");
  }

  @Test
  public void test_conference_room_not_available_due_to_maintenance()
      throws ConferenceRoomNotAvailableException {
    // Given
    ReservationDTO reservationDTO =
        ReservationDTO.builder()
            .roomCapacity(10)
            .locationId(1)
            .meetingDate(LocalDate.now().plusDays(1))
            .startTime(LocalTime.of(9, 0))
            .endTime(LocalTime.of(10, 0))
            .build();

    when(roomService.findConferenceRoomsMatchingCapacity(
            reservationDTO.getRoomCapacity(), reservationDTO.getLocationId()))
        .thenReturn(getRoomList());

    List<Integer> reservationList = Arrays.asList(0, 1);
    when(reservationRepository.findReservationsAndMaintenance(
            LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(10, 0), 1))
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
    ReservationDTO reservationDTO =
        ReservationDTO.builder()
            .roomCapacity(10)
            .locationId(1)
            .meetingDate(LocalDate.now().plusDays(1))
            .startTime(LocalTime.of(9, 0))
            .endTime(LocalTime.of(10, 0))
            .build();

    when(roomService.findConferenceRoomsMatchingCapacity(
            reservationDTO.getRoomCapacity(), reservationDTO.getLocationId()))
        .thenReturn(getRoomList());

    List<Integer> reservationList = Arrays.asList(1);
    when(reservationRepository.findReservationsAndMaintenance(
            LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(10, 0), 1))
        .thenReturn(reservationList);

    // When
    Exception e =
        assertThrows(
            ConferenceRoomNotAvailableException.class,
            () -> {
              reservationService.reserveConferenceRoom(reservationDTO);
            });
    assertEquals(
        e.getMessage(),
        "Conference room not available for given time period. Check availability before reservation");
  }

  private List<ConferenceRoomDTO> getRoomList() {
    List<ConferenceRoomDTO> roomList = new ArrayList<>();
    ConferenceRoomDTO room =
        ConferenceRoomDTO.builder().id(1).roomName("Room 1").roomCapacity(10).locationId(1).build();
    roomList.add(room);
    return roomList;
  }
}
