package com.lms.controller;

import com.lms.dto.GradeRequest;
import com.lms.dto.SubmissionRequest;
import com.lms.dto.SubmissionResponse;
import com.lms.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    // STUDENT - submit assignment
    @PostMapping("/submit/{assignmentId}")
    public ResponseEntity<SubmissionResponse> submit(
            @PathVariable Long assignmentId,
            @RequestBody SubmissionRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(submissionService.submit(assignmentId, request, email));
    }

    // STUDENT - view my submissions
    @GetMapping("/my")
    public ResponseEntity<List<SubmissionResponse>> getMySubmissions(
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(submissionService.getMySubmissions(email));
    }

    // INSTRUCTOR - view all submissions for an assignment
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<SubmissionResponse>> getSubmissionsByAssignment(
            @PathVariable Long assignmentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByAssignment(assignmentId));
    }

    // INSTRUCTOR - grade a submission
    @PutMapping("/grade/{submissionId}")
    public ResponseEntity<SubmissionResponse> gradeSubmission(
            @PathVariable Long submissionId,
            @RequestBody GradeRequest request) {
        return ResponseEntity.ok(submissionService.gradeSubmission(submissionId, request));
    }
}
