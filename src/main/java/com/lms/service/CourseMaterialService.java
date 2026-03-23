package com.lms.service;

import com.lms.dto.CourseMaterialDto;
import com.lms.entity.Course;
import com.lms.entity.CourseMaterial;
import com.lms.repository.CourseMaterialRepository;
import com.lms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseMaterialService {

    @Autowired
    private CourseMaterialRepository courseMaterialRepository;

    @Autowired
    private CourseRepository courseRepository;

    public CourseMaterialDto addMaterial(Long courseId, CourseMaterialDto dto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        CourseMaterial material = new CourseMaterial();
        material.setTitle(dto.getTitle());
        material.setFileUrl(dto.getFileUrl());
        material.setCourse(course);

        courseMaterialRepository.save(material);
        return mapToDto(material);
    }

    public List<CourseMaterialDto> getMaterialsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        return courseMaterialRepository.findByCourse(course)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CourseMaterialDto mapToDto(CourseMaterial material) {
        CourseMaterialDto dto = new CourseMaterialDto();
        dto.setId(material.getId());
        dto.setTitle(material.getTitle());
        dto.setFileUrl(material.getFileUrl());
        dto.setCourseId(material.getCourse().getId());
        dto.setCourseTitle(material.getCourse().getTitle());
        return dto;
    }
}