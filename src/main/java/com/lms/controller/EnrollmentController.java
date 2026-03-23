package com.lms.controller;

import com.lms.dto.EnrollmentResponse;
import com.lms.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    // STUDENT - enroll in a course
    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<EnrollmentResponse> enroll(
            @PathVariable Long courseId,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(enrollmentService.enroll(courseId, email));
    }

    // STUDENT - view my enrollments
    @GetMapping("/my")
    public ResponseEntity<List<EnrollmentResponse>> getMyEnrollments(
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(enrollmentService.getMyEnrollments(email));
    }

    // INSTRUCTOR - view students in a course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    // STUDENT - update progress
    @PutMapping("/progress/{courseId}")
    public ResponseEntity<EnrollmentResponse> updateProgress(
            @PathVariable Long courseId,
            @RequestParam int progress,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(enrollmentService.updateProgress(courseId, email, progress));
    }
}
