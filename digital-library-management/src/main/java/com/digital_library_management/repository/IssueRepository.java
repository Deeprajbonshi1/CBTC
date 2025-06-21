package com.digital_library_management.repository;
import com.digital_library_management.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByStatus(Issue.IssueStatus status);
    List<Issue> findByUserIdAndStatus(Long userId, Issue.IssueStatus status);

}