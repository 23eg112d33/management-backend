package com.lms.service;

import com.lms.dto.AssignmentRequest;
import com.lms.dto.AssignmentResponse;
import com.lms.entity.Assignment;
import com.lms.entity.Course;
import com.lms.entity.User;
import com.lms.repository.AssignmentRepository;
import com.lms.repository.CourseRepository;
import com.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    // Instructor creates assignment
    public AssignmentResponse createAssignment(Long courseId,
                                                AssignmentRequest request,
                                                String email) {
        User instructor = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        if (!course.getInstructor().getId().equals(instructor.getId())) {
            throw new RuntimeException("You are not the instructor of this course!");
        }

        Assignment assignment = new Assignment();
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDueDate(LocalDate.parse(request.getDueDate()));
        assignment.setCourse(course);

        assignmentRepository.save(assignment);
        return mapToResponse(assignment);
    }

    // Get assignments by course
    public List<AssignmentResponse> getAssignmentsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        return assignmentRepository.findByCourse(course)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AssignmentResponse mapToResponse(Assignment assignment) {
        AssignmentResponse response = new AssignmentResponse();
        response.setId(assignment.getId());
        response.setTitle(assignment.getTitle());
        response.setDescription(assignment.getDescription());
        response.setDueDate(assignment.getDueDate().toString());
        response.setCourseTitle(assignment.getCourse().getTitle());
        return response;
    }
}
