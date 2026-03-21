package com.gym.service;

import com.gym.dto.CourseDTO;
import com.gym.dto.CourseScheduleDTO;
import com.gym.entity.*;
import com.gym.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final CourseScheduleRepository scheduleRepository;
    private final CoachRepository coachRepository;
    
    public List<Course> findAllCourses() {
        return courseRepository.findByEnabledTrue();
    }
    
    public Course findCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public Course createCourse(CourseDTO dto) {
        Course course = Course.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .duration(dto.getDuration())
                .maxParticipants(dto.getMaxParticipants())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .enabled(true)
                .build();
        return courseRepository.save(course);
    }
    
    @Transactional
    public Course updateCourse(Long id, CourseDTO dto) {
        Course course = findCourseById(id);
        if (course == null) throw new RuntimeException("课程不存在");
        
        if (dto.getName() != null) course.setName(dto.getName());
        if (dto.getDescription() != null) course.setDescription(dto.getDescription());
        if (dto.getCategory() != null) course.setCategory(dto.getCategory());
        if (dto.getDuration() != null) course.setDuration(dto.getDuration());
        if (dto.getMaxParticipants() != null) course.setMaxParticipants(dto.getMaxParticipants());
        if (dto.getPrice() != null) course.setPrice(dto.getPrice());
        if (dto.getImageUrl() != null) course.setImageUrl(dto.getImageUrl());
        
        return courseRepository.save(course);
    }
    
    @Transactional
    public void deleteCourse(Long id) {
        Course course = findCourseById(id);
        if (course != null) {
            course.setEnabled(false);
            courseRepository.save(course);
        }
    }
    
    // 排课
    @Transactional
    public CourseSchedule createSchedule(CourseScheduleDTO dto) {
        CourseSchedule schedule = CourseSchedule.builder()
                .courseId(dto.getCourseId())
                .coachId(dto.getCoachId())
                .location(dto.getLocation())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .currentParticipants(0)
                .enabled(true)
                .build();
        
        return scheduleRepository.save(schedule);
    }
    
    public List<CourseSchedule> findAllSchedules() {
        return scheduleRepository.findByEnabledTrue();
    }
    
    public List<CourseSchedule> findSchedulesByCoach(Long coachId) {
        return scheduleRepository.findByCoachId(coachId);
    }
    
    public List<CourseSchedule> findSchedulesByTimeRange(LocalDateTime start, LocalDateTime end) {
        return scheduleRepository.findByTimeRange(start, end);
    }
    
    @Transactional
    public CourseSchedule updateSchedule(Long id, CourseScheduleDTO dto) {
        CourseSchedule schedule = scheduleRepository.findById(id).orElse(null);
        if (schedule == null) throw new RuntimeException("课表不存在");
        
        if (dto.getLocation() != null) schedule.setLocation(dto.getLocation());
        if (dto.getStartTime() != null) schedule.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) schedule.setEndTime(dto.getEndTime());
        
        return scheduleRepository.save(schedule);
    }
    
    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.findById(id).ifPresent(schedule -> {
            schedule.setEnabled(false);
            scheduleRepository.save(schedule);
        });
    }
}