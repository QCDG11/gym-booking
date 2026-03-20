package com.gym.repository;

import com.gym.entity.CourseBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseBookingRepository extends JpaRepository<CourseBooking, Long> {
    List<CourseBooking> findByUserId(Long userId);
    List<CourseBooking> findByScheduleId(Long scheduleId);
    Optional<CourseBooking> findByScheduleIdAndUserId(Long scheduleId, Long userId);
    boolean existsByScheduleIdAndUserId(Long scheduleId, Long userId);
}