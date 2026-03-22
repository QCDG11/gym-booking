package com.gym.service;

import com.gym.entity.*;
import com.gym.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    
    private final CourseBookingRepository courseBookingRepository;
    private final CourseScheduleRepository scheduleRepository;
    private final PrivateTrainingRepository privateTrainingRepository;
    private final PrivateTrainingBookingRepository privateBookingRepository;
    
    // ===== 课程预约 =====
    @Transactional
    public CourseBooking bookCourse(Long scheduleId, Long userId) {
        CourseSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("课表不存在"));
        
        if (schedule.getCurrentParticipants() >= 20) {
            throw new RuntimeException("课程已满");
        }
        
        if (courseBookingRepository.existsByScheduleIdAndUserId(scheduleId, userId)) {
            throw new RuntimeException("您已预约过此课程");
        }
        
        CourseBooking booking = CourseBooking.builder()
                .scheduleId(scheduleId)
                .userId(userId)
                .status(CourseBooking.BookingStatus.CONFIRMED)
                .build();
        
        booking = courseBookingRepository.save(booking);
        
        // 更新已预约人数
        schedule.setCurrentParticipants(schedule.getCurrentParticipants() + 1);
        scheduleRepository.save(schedule);
        
        return booking;
    }
    
    @Transactional
    public void cancelCourseBooking(Long bookingId) {
        CourseBooking booking = courseBookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("预约不存在"));
        
        if (booking.getStatus() == CourseBooking.BookingStatus.CANCELLED) {
            throw new RuntimeException("预约已取消");
        }
        
        booking.setStatus(CourseBooking.BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        courseBookingRepository.save(booking);
        
        // 减少已预约人数
        if (booking.getScheduleId() != null) {
            CourseSchedule schedule = scheduleRepository.findById(booking.getScheduleId()).orElse(null);
            if (schedule != null) {
                schedule.setCurrentParticipants(Math.max(0, schedule.getCurrentParticipants() - 1));
                scheduleRepository.save(schedule);
            }
        }
    }
    
    public List<CourseBooking> findUserCourseBookings(Long userId) {
        return courseBookingRepository.findByUserId(userId);
    }
    
    public List<CourseBooking> findScheduleBookings(Long scheduleId) {
        return courseBookingRepository.findByScheduleId(scheduleId);
    }
    
    // ===== 私教预约 =====
    @Transactional
    public PrivateTraining createPrivateTraining(Long coachId, Long userId, PrivateTraining training) {
        Coach coach = new Coach();
        coach.setId(coachId);
        User user = new User();
        user.setId(userId);
        
        training.setCoach(coach);
        training.setUser(user);
        training.setStatus(PrivateTraining.TrainingStatus.ACTIVE);
        
        return privateTrainingRepository.save(training);
    }
    
    public List<PrivateTraining> findUserPrivateTrainings(Long userId) {
        return privateTrainingRepository.findByUserId(userId);
    }
    
    public List<PrivateTraining> findCoachPrivateTrainings(Long coachId) {
        return privateTrainingRepository.findByCoachId(coachId);
    }
    
    @Transactional
    public PrivateTrainingBooking bookPrivateTraining(Long trainingId, LocalDateTime bookedTime, String location, String note) {
        PrivateTraining training = privateTrainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("私教服务不存在"));
        
        if (training.getRemainingSessions() <= 0) {
            throw new RuntimeException("剩余次数不足");
        }
        
        PrivateTrainingBooking booking = PrivateTrainingBooking.builder()
                .privateTraining(training)
                .bookedTime(bookedTime)
                .location(location)
                .note(note)
                .status(PrivateTrainingBooking.BookingStatus.CONFIRMED)
                .build();
        
        booking = privateBookingRepository.save(booking);
        
        // 减少剩余次数
        training.setRemainingSessions(training.getRemainingSessions() - 1);
        privateTrainingRepository.save(training);
        
        return booking;
    }
    
    @Transactional
    public void cancelPrivateBooking(Long bookingId) {
        PrivateTrainingBooking booking = privateBookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("预约不存在"));
        
        booking.setStatus(PrivateTrainingBooking.BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        privateBookingRepository.save(booking);
        
        // 恢复次数
        PrivateTraining training = booking.getPrivateTraining();
        training.setRemainingSessions(training.getRemainingSessions() + 1);
        privateTrainingRepository.save(training);
    }
    
    public List<PrivateTrainingBooking> findUserPrivateBookings(Long userId) {
        return privateBookingRepository.findByPrivateTrainingUserId(userId);
    }
    
    public List<PrivateTrainingBooking> findPrivateBookingsByTraining(Long trainingId) {
        return privateBookingRepository.findByPrivateTrainingId(trainingId);
    }
}