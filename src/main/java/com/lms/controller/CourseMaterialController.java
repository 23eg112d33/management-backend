package com.lms.controller;

import com.lms.dto.CourseMaterialDto;
import com.lms.service.CourseMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class CourseMaterialController {

    @Autowired
    private CourseMaterialService courseMaterialService;

    @PostMapping("/add/{courseId}")
    public ResponseEntity<CourseMaterialDto> addMaterial(
            @PathVariable Long courseId,
            @RequestBody CourseMaterialDto dto) {
        return ResponseEntity.ok(courseMaterialService.addMaterial(courseId, dto));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseMaterialDto>> getMaterials(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(courseMaterialService.getMaterialsByCourse(courseId));
    }
}