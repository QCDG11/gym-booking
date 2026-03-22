package com.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseBookingDTO {
    private Long id;
    private Long scheduleId;
    private Long userId;
    private String status;
    private String bookedAt;
    private String cancelledAt;
    
    // 课表详情
    private String courseName;
    private String coachName;
    private String location;
    private String startTime;
    private Integer maxParticipants;
    private Integer currentParticipants;
}