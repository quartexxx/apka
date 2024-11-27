package com.example.innovation_app.repository;

import com.example.innovation_app.model.InnovationSubmission;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@ComponentScan
@Repository
public interface SubmissionRepository extends JpaRepository<InnovationSubmission, Long> {
List<InnovationSubmission> findByUserId(Long userId);
    @Modifying
    @Transactional
    @Query("UPDATE InnovationSubmission s SET s.userId = NULL WHERE s.userId = :userId")
    void setUserIdToNullForUser(@Param("userId") Long userId);
}
