package com.gym.controller;

import com.gym.entity.*;
import com.gym.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coach")
@RequiredArgsConstructor
public class CoachController {
    
    private final CoachService coachService;
    private final CourseService courseService;
    private final BookingService bookingService;
    
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        Coach coach = coachService.findByUserId(user.getId());
        return ResponseEntity.ok(coach);
    }
    
    @GetMapping("/schedules")
    public ResponseEntity<?> getMySchedules(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        Coach coach = coachService.findByUserId(user.getId());
        if (coach == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "教练信息不存在"));
        }
        
        List<CourseSchedule> schedules = courseService.findSchedulesByCoach(coach.getId());
        return ResponseEntity.ok(schedules);
    }
    
    @GetMapping("/schedules/{scheduleId}/bookings")
    public ResponseEntity<?> getScheduleBookings(@PathVariable Long scheduleId) {
        List<CourseBooking> bookings = bookingService.findScheduleBookings(scheduleId);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/private-trainings")
    public ResponseEntity<?> getMyPrivateTrainings(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        Coach coach = coachService.findByUserId(user.getId());
        if (coach == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "教练信息不存在"));
        }
        
        List<PrivateTraining> trainings = bookingService.findCoachPrivateTrainings(coach.getId());
        return ResponseEntity.ok(trainings);
    }
    
    @GetMapping("/private-bookings/{trainingId}")
    public ResponseEntity<?> getPrivateBookings(@PathVariable Long trainingId) {
        List<PrivateTrainingBooking> bookings = bookingService.findPrivateBookingsByTraining(trainingId);
        return ResponseEntity.ok(bookings);
    }
}