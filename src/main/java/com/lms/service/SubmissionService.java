package com.lms.service;

import com.lms.dto.GradeRequest;
import com.lms.dto.SubmissionRequest;
import com.lms.dto.SubmissionResponse;
import com.lms.entity.Assignment;
import com.lms.entity.Submission;
import com.lms.entity.SubmissionStatus;
import com.lms.entity.User;
import com.lms.repository.AssignmentRepository;
import com.lms.repository.SubmissionRepository;
import com.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    public SubmissionResponse submit(Long assignmentId,
                                      SubmissionRequest request,
                                      String email) {
        User student = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found!"));

        if (submissionRepository.existsByStudentAndAssignment(student, assignment)) {
            throw new RuntimeException("Already submitted this assignment!");
        }

        Submission submission = new Submission();
        submission.setStudent(student);
        submission.setAssignment(assignment);
        submission.setFileUrl(request.getFileUrl());
        submission.setStatus(SubmissionStatus.SUBMITTED);

        submissionRepository.save(submission);
        return mapToResponse(submission);
    }

    public List<SubmissionResponse> getMySubmissions(String email) {
        User student = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        return submissionRepository.findByStudent(student)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<SubmissionResponse> getSubmissionsByAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found!"));

        return submissionRepository.findByAssignment(assignment)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SubmissionResponse gradeSubmission(Long submissionId, GradeRequest request) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found!"));

        submission.setMarks(request.getMarks());
        submission.setFeedback(request.getFeedback());
        submission.setStatus(SubmissionStatus.GRADED);

        submissionRepository.save(submission);
        return mapToResponse(submission);
    }

    private SubmissionResponse mapToResponse(Submission submission) {
        SubmissionResponse response = new SubmissionResponse();
        response.setId(submission.getId());
        response.setStudentName(submission.getStudent().getName());
        response.setAssignmentTitle(submission.getAssignment().getTitle());
        response.setFileUrl(submission.getFileUrl());
        response.setMarks(submission.getMarks());
        response.setFeedback(submission.getFeedback());
        response.setStatus(submission.getStatus().name());
        return response;
    }
}
   