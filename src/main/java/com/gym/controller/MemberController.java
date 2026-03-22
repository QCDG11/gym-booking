package com.gym.controller;

import com.gym.dto.*;
import com.gym.entity.*;
import com.gym.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    
    private final CourseService courseService;
    private final CoachService coachService;
    private final BookingService bookingService;
    
    // ===== 课程浏览与预约 =====
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getCourses() {
        return ResponseEntity.ok(courseService.findAllCourses());
    }
    
    @GetMapping("/schedules")
    public ResponseEntity<List<CourseSchedule>> getSchedules(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        if (startDate != null && endDate != null) {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            return ResponseEntity.ok(courseService.findSchedulesByTimeRange(start, end));
        }
        
        return ResponseEntity.ok(courseService.findAllSchedules());
    }
    
    @PostMapping("/book/course/{scheduleId}")
    public ResponseEntity<?> bookCourse(@AuthenticationPrincipal User user, @PathVariable Long scheduleId) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        try {
            CourseBooking booking = bookingService.bookCourse(scheduleId, user.getId());
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @DeleteMapping("/book/course/{bookingId}")
    public ResponseEntity<?> cancelCourseBooking(@AuthenticationPrincipal User user, @PathVariable Long bookingId) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        try {
            bookingService.cancelCourseBooking(bookingId);
            return ResponseEntity.ok(Map.of("message", "预约已取消"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @GetMapping("/my-course-bookings")
    public ResponseEntity<?> getMyCourseBookings(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        List<CourseBookingDTO> bookings = bookingService.findUserCourseBookings(user.getId());
        return ResponseEntity.ok(bookings);
    }
    
    // ===== 私教浏览与预约 =====
    @GetMapping("/coaches")
    public ResponseEntity<?> getCoaches() {
        List<Coach> coaches = coachService.findAll();
        List<Map<String, Object>> result = coaches.stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("name", c.getName());
            map.put("avatar", c.getAvatar());
            map.put("phone", c.getPhone());
            map.put("specialty", c.getSpecialty());
            map.put("introduction", c.getIntroduction());
            map.put("yearsOfExperience", c.getYearsOfExperience());
            map.put("enabled", c.getEnabled());
            return map;
        }).toList();
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/book/private")
    public ResponseEntity<?> bookPrivateTraining(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, Object> request) {
        
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        try {
            Long coachId = Long.valueOf(request.get("coachId").toString());
            Integer totalSessions = Integer.valueOf(request.get("totalSessions").toString());
            
            PrivateTraining training = PrivateTraining.builder()
                    .type((String) request.get("type"))
                    .duration((Integer) request.get("duration"))
                    .price(new java.math.BigDecimal(request.get("price").toString()))
                    .totalSessions(totalSessions)
                    .remainingSessions(totalSessions)
                    .expireDate(LocalDateTime.now().plusMonths(3))
                    .build();
            
            training = bookingService.createPrivateTraining(coachId, user.getId(), training);
            return ResponseEntity.ok(training);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @PostMapping("/book/private/{trainingId}")
    public ResponseEntity<?> bookPrivateSession(
            @AuthenticationPrincipal User user,
            @PathVariable Long trainingId,
            @RequestBody Map<String, Object> request) {
        
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        try {
            LocalDateTime bookedTime = LocalDateTime.parse((String) request.get("bookedTime"));
            String location = (String) request.get("location");
            String note = (String) request.get("note");
            
            PrivateTrainingBooking booking = bookingService.bookPrivateTraining(
                    trainingId, bookedTime, location, note);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @GetMapping("/my-private-trainings")
    public ResponseEntity<?> getMyPrivateTrainings(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        List<PrivateTrainingDTO> trainings = bookingService.findUserPrivateTrainings(user.getId());
        return ResponseEntity.ok(trainings);
    }
    
    @GetMapping("/my-private-bookings")
    public ResponseEntity<?> getMyPrivateBookings(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        List<PrivateTrainingBooking> bookings = bookingService.findUserPrivateBookings(user.getId());
        return ResponseEntity.ok(bookings);
    }
    
    @DeleteMapping("/book/private/booking/{bookingId}")
    public ResponseEntity<?> cancelPrivateBooking(@AuthenticationPrincipal User user, @PathVariable Long bookingId) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        try {
            bookingService.cancelPrivateBooking(bookingId);
            return ResponseEntity.ok(Map.of("message", "预约已取消"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}