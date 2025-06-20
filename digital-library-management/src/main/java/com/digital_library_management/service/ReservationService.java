package com.digital_library_management.service;
import com.digital_library_management.entity.Book;
import com.digital_library_management.entity.Reservation;
import com.digital_library_management.entity.User;
import com.digital_library_management.repository.BookRepository;
import com.digital_library_management.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<Reservation> getPendingReservations() {
        return reservationRepository.findByStatus(Reservation.ReservationStatus.PENDING);
    }

    public List<Reservation> getUserReservations(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Transactional
    public Reservation createReservation(Long userId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceClosedException("Book not found"));
        if (book.getQuantityAvailable() > 0) {
            throw new RuntimeException("Book is available—no need to reserve");
        }
        Reservation res = Reservation.builder()
                .user(User.builder().id(userId).build())
                .book(book)
                .reservationDate(LocalDate.now())
                .status(Reservation.ReservationStatus.PENDING)
                .build();
        return reservationRepository.save(res);
    }

    @Transactional
    public void cancelReservation(Long resId) {
        Reservation res = reservationRepository.findById(resId)
                .orElseThrow(() -> new ResourceClosedException("Reservation not found"));
        if (res.getStatus() != Reservation.ReservationStatus.PENDING) {
            throw new RuntimeException("Cannot cancel—already processed");
        }
        res.setStatus(Reservation.ReservationStatus.CANCELLED);
        reservationRepository.save(res);
    }
}