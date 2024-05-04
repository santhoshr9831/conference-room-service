package com.conference.repository;

import com.conference.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

  @Query(
      nativeQuery = true,
      value =
          "select room_id from CNF_ROOM_RESERVATIONS where meeting_date=:meetingDate and start_time < :endTime and  end_time > :startTime\n"
              + "union\n"
              + "select room_id from CNF_ROOM_MAINTENANCE where mnt_start_time < :endTime and  mnt_end_time > :startTime and location_id=:location order by room_id\n")
  List<Integer> findReservationsAndMaintenance(
      LocalDate meetingDate, LocalTime startTime, LocalTime endTime, int location);
}
