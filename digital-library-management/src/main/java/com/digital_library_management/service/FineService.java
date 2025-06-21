package com.digital_library_management.service;
import com.digital_library_management.entity.Fine;
import com.digital_library_management.repository.FineRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FineService {

    @Autowired
    private FineRepository fineRepository;

    public List<Fine> getUnpaidFines() {
        return fineRepository.findByPaidFalse();
    }

    public List<Fine> getUserFines(Long userId) {
        return fineRepository.findByIssueUserId(userId);
    }

    @Transactional
    public Fine payFine(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new ResourceClosedException("Fine not found"));
        if (fine.isPaid()) {
            throw new RuntimeException("Fine already paid");
        }
        fine.setPaid(true);
        fine.setPaidDate(java.time.LocalDate.now());
        return fineRepository.save(fine);
    }
}
