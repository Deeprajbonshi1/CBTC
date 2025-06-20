package com.digital_library_management.repository;

import com.digital_library_management.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatus(Reservation.ReservationStatus status);
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByBookIdAndStatusOrderByReservationDateAsc(Long bookId, Reservation.ReservationStatus status);
}