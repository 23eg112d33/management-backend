package com.lms.controller;

import com.lms.dto.CourseRequest;
import com.lms.dto.CourseResponse;
import com.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // INSTRUCTOR - create course
    @PostMapping("/create")
    public ResponseEntity<CourseResponse> createCourse(
            @RequestBody CourseRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(courseService.createCourse(request, email));
    }

    // STUDENT - view all approved courses
    @GetMapping("/all")
    public ResponseEntity<List<CourseResponse>> getApprovedCourses() {
        return ResponseEntity.ok(courseService.getApprovedCourses());
    }

    // INSTRUCTOR - view my courses
    @GetMapping("/my")
    public ResponseEntity<List<CourseResponse>> getMyCourses(
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(courseService.getMyCourses(email));
    }

    // ADMIN - view all courses
    @GetMapping("/admin/all")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    // ADMIN - approve or reject course
    @PutMapping("/admin/status/{courseId}")
    public ResponseEntity<CourseResponse> updateStatus(
            @PathVariable Long courseId,
            @RequestParam String status) {
        return ResponseEntity.ok(courseService.updateCourseStatus(courseId, status));
    }
}
