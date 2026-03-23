package com.lms.repository;

import com.lms.entity.Assignment;
import com.lms.entity.Submission;
import com.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("SELECT s FROM Submission s WHERE s.assignment = :assignment")
    List<Submission> findByAssignment(@Param("assignment") Assignment assignment);

    @Query("SELECT s FROM Submission s WHERE s.student = :student AND s.assignment = :assignment")
    Optional<Submission> findByStudentAndAssignment(@Param("student") User student, @Param("assignment") Assignment assignment);

    @Query("SELECT s FROM Submission s WHERE s.student = :student")
    List<Submission> findByStudent(@Param("student") User student);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Submission s WHERE s.student = :student AND s.assignment = :assignment")
    boolean existsByStudentAndAssignment(@Param("student") User student, @Param("assignment") Assignment assignment);
}