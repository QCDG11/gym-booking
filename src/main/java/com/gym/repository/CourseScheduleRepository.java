package com.gym.repository;

import com.gym.entity.CourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, Long> {
    List<CourseSchedule> findByEnabledTrue();
    
    List<CourseSchedule> findByCoachId(Long coachId);
    
    @Query("SELECT cs FROM CourseSchedule cs WHERE cs.startTime >= :start AND cs.endTime <= :end")
    List<CourseSchedule> findByTimeRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT cs FROM CourseSchedule cs WHERE cs.coachId = :coachId AND cs.startTime >= :start AND cs.endTime <= :end")
    List<CourseSchedule> findByCoachIdAndTimeRange(@Param("coachId") Long coachId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}