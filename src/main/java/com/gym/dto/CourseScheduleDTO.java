package com.gym.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseScheduleDTO {
    private Long id;
    private Long courseId;
    private String courseName;
    private Long coachId;
    private String coachName;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer currentParticipants;
    private Integer maxParticipants;
}