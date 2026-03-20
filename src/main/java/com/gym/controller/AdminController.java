package com.gym.controller;

import com.gym.dto.*;
import com.gym.entity.*;
import com.gym.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final CoachService coachService;
    private final CourseService courseService;
    private final BookingService bookingService;
    
    // ===== 教练管理 =====
    @GetMapping("/coaches")
    public ResponseEntity<List<Coach>> getAllCoaches() {
        return ResponseEntity.ok(coachService.findAll());
    }
    
    @PostMapping("/coaches")
    public ResponseEntity<?> createCoach(@RequestBody Map<String, Object> request) {
        try {
            CoachDTO dto = new CoachDTO();
            dto.setName((String) request.get("name"));
            dto.setAvatar((String) request.get("avatar"));
            dto.setPhone((String) request.get("phone"));
            dto.setSpecialty((String) request.get("specialty"));
            dto.setIntroduction((String) request.get("introduction"));
            dto.setYearsOfExperience((Integer) request.get("yearsOfExperience"));
            
            String username = (String) request.get("username");
            String password = (String) request.get("password");
            
            Coach coach = coachService.createCoach(dto, username, password);
            return ResponseEntity.ok(coach);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @PutMapping("/coaches/{id}")
    public ResponseEntity<?> updateCoach(@PathVariable Long id, @RequestBody CoachDTO dto) {
        try {
            Coach coach = coachService.updateCoach(id, dto);
            return ResponseEntity.ok(coach);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @DeleteMapping("/coaches/{id}")
    public ResponseEntity<?> deleteCoach(@PathVariable Long id) {
        coachService.deleteCoach(id);
        return ResponseEntity.ok(Map.of("message", "教练已删除"));
    }
    
    // ===== 课程管理 =====
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.findAllCourses());
    }
    
    @PostMapping("/courses")
    public ResponseEntity<?> createCourse(@RequestBody CourseDTO dto) {
        try {
            Course course = courseService.createCourse(dto);
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @PutMapping("/courses/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody CourseDTO dto) {
        try {
            Course course = courseService.updateCourse(id, dto);
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(Map.of("message", "课程已删除"));
    }
    
    // ===== 排课管理 =====
    @GetMapping("/schedules")
    public ResponseEntity<List<CourseSchedule>> getAllSchedules() {
        return ResponseEntity.ok(courseService.findAllSchedules());
    }
    
    @PostMapping("/schedules")
    public ResponseEntity<?> createSchedule(@RequestBody CourseScheduleDTO dto) {
        try {
            CourseSchedule schedule = courseService.createSchedule(dto);
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @PutMapping("/schedules/{id}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long id, @RequestBody CourseScheduleDTO dto) {
        try {
            CourseSchedule schedule = courseService.updateSchedule(id, dto);
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long id) {
        courseService.deleteSchedule(id);
        return ResponseEntity.ok(Map.of("message", "课表已删除"));
    }
    
    // ===== 预约管理 =====
    @GetMapping("/course-bookings")
    public ResponseEntity<List<CourseBooking>> getAllCourseBookings() {
        return ResponseEntity.ok(bookingService.findUserCourseBookings(null));
    }
    
    @GetMapping("/schedule-bookings/{scheduleId}")
    public ResponseEntity<List<CourseBooking>> getScheduleBookings(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(bookingService.findScheduleBookings(scheduleId));
    }
    
    @GetMapping("/private-bookings")
    public ResponseEntity<List<PrivateTrainingBooking>> getAllPrivateBookings() {
        return ResponseEntity.ok(bookingService.findUserPrivateBookings(null));
    }
}