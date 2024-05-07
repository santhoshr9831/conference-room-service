package com.conference.service;

import com.conference.dto.ConferenceRoomAvailabilityDTO;
import com.conference.dto.ConferenceRoomDTO;
import com.conference.dto.ReservationDTO;
import com.conference.entity.ConferenceRoom;
import com.conference.repository.ConferenceRoomRepository;
import com.conference.repository.ReservationRepository;
import com.conference.service.impl.AvailabilityServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceTest {

  @InjectMocks AvailabilityServiceImpl availabilityService;

  @Mock ReservationService reservationService;

  @Mock ConferenceRoomService conferenceRoomService;

  @Test
  public void test_list_of_conference_rooms_with_availability() {
    // Given
    LocalTime startTime = LocalTime.of(9, 0);
    LocalTime endTime = LocalTime.of(10, 0);
    List<Integer> reservationsList = Collections.EMPTY_LIST;

    ReservationDTO reservationDTO =
        ReservationDTO.builder()
            .meetingDate(LocalDate.now())
            .startTime(startTime)
            .endTime(endTime)
            .locationId(1)
            .build();

    when(reservationService.findReservationsAndMaintenance(reservationDTO))
        .thenReturn(reservationsList);
    when(conferenceRoomService.findConferenceRoomsByLocation(1)).thenReturn(getRoomList());

    // When
    List<ConferenceRoomAvailabilityDTO> result =
        availabilityService.getConferenceRoomsAvailability(startTime, endTime, 1);

    assertEquals(1, result.size());
    assertEquals("Room 1", result.get(0).getRoomName());
    assertEquals(10, result.get(0).getRoomCapacity());
    assertTrue(result.get(0).getAvailable());
  }

  @Test
  public void test_no_available_conference_rooms() {
    // Given
    LocalTime startTime = LocalTime.of(9, 0);
    LocalTime endTime = LocalTime.of(10, 0);
    List<Integer> reservationsList = Arrays.asList(1);
    ReservationDTO reservationDTO =
        ReservationDTO.builder()
            .meetingDate(LocalDate.now())
            .startTime(startTime)
            .endTime(endTime)
            .locationId(1)
            .build();

    when(reservationService.findReservationsAndMaintenance(reservationDTO))
        .thenReturn(reservationsList);
    when(conferenceRoomService.findConferenceRoomsByLocation(1)).thenReturn(getRoomList());

    // When
    List<ConferenceRoomAvailabilityDTO> result =
        availabilityService.getConferenceRoomsAvailability(startTime, endTime, 1);

    assertEquals(1, result.size());
    assertEquals("Room 1", result.get(0).getRoomName());
    assertEquals(10, result.get(0).getRoomCapacity());
    assertFalse(result.get(0).getAvailable());
  }

  private List<ConferenceRoomDTO> getRoomList() {
    List<ConferenceRoomDTO> roomList = new ArrayList<>();
    ConferenceRoomDTO room =
        ConferenceRoomDTO.builder().id(1).roomName("Room 1").roomCapacity(10).locationId(1).build();
    roomList.add(room);
    return roomList;
  }
}
