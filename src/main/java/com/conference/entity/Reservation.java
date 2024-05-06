package com.conference.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Table(name = "cnf_room_reservations")
@Entity
public class Reservation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_id")
  private Integer reservationId;

  @Column(name = "room_id")
  private Integer roomId;

  @Column(name = "meeting_Date")
  private LocalDate meetingDate;

  private LocalTime startTime;

  private LocalTime endTime;

  @Column(name = "is_active")
  private Boolean isActive = true;
}
