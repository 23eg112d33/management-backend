package com.lms.service;

import com.lms.dto.EnrollmentResponse;
import com.lms.entity.Course;
import com.lms.entity.CourseStatus;
import com.lms.entity.Enrollment;
import com.lms.entity.User;
import com.lms.repository.CourseRepository;
import com.lms.repository.EnrollmentRepository;
import com.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Student enrolls in a course
    public EnrollmentResponse enroll(Long courseId, String email) {
        User student = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        if (course.getStatus() != CourseStatus.APPROVED) {
            throw new RuntimeException("Course is not approved yet!");
        }

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new RuntimeException("Already enrolled in this course!");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setProgressPercent(0);

        enrollmentRepository.save(enrollment);
        return mapToResponse(enrollment);
    }

    // Student views their enrollments
    public List<EnrollmentResponse> getMyEnrollments(String email) {
        User student = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        return enrollmentRepository.findByStudent(student)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Instructor views students in their course
    public List<EnrollmentResponse> getEnrollmentsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        return enrollmentRepository.findByCourse(course)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Update progress
    public EnrollmentResponse updateProgress(Long courseId, String email, int progress) {
        User student = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new RuntimeException("Enrollment not found!"));

        enrollment.setProgressPercent(progress);
        enrollmentRepository.save(enrollment);
        return mapToResponse(enrollment);
    }

    private EnrollmentResponse mapToResponse(Enrollment enrollment) {
        EnrollmentResponse response = new EnrollmentResponse();
        response.setId(enrollment.getId());
        response.setStudentName(enrollment.getStudent().getName());
        response.setCourseTitle(enrollment.getCourse().getTitle());
        response.setProgressPercent(enrollment.getProgressPercent());
        return response;
    }
}