package com.digital_library_management.repository;

import com.digital_library_management.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByPaidFalse();
    List<Fine> findByIssueUserId(Long userId);
}