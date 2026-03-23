package com.lms.service;

import com.lms.dto.CourseRequest;
import com.lms.dto.CourseResponse;
import com.lms.entity.Course;
import com.lms.entity.CourseStatus;
import com.lms.entity.User;
import com.lms.repository.CourseRepository;
import com.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    // Instructor creates a course
    public CourseResponse createCourse(CourseRequest request, String email) {
        User instructor = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setStatus(CourseStatus.PENDING);
        course.setInstructor(instructor);

        courseRepository.save(course);
        return mapToResponse(course);
    }

    // Get all approved courses (for students)
    public List<CourseResponse> getApprovedCourses() {
        return courseRepository.findByStatus(CourseStatus.APPROVED)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get courses by instructor
    public List<CourseResponse> getMyCourses(String email) {
        User instructor = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        return courseRepository.findByInstructor(instructor)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Admin gets all courses
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Admin approves or rejects course
    public CourseResponse updateCourseStatus(Long courseId, String status) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        course.setStatus(CourseStatus.valueOf(status.toUpperCase()));
        courseRepository.save(course);
        return mapToResponse(course);
    }

    private CourseResponse mapToResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setDescription(course.getDescription());
        response.setStatus(course.getStatus().name());
        response.setInstructorName(course.getInstructor().getName());
        return response;
    }
}