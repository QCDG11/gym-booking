package com.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivateTrainingDTO {
    private Long id;
    private Long coachId;
    private Long userId;
    private String type;
    private Integer duration;
    private BigDecimal price;
    private Integer totalSessions;
    private Integer remainingSessions;
    private String status;
    private String expireDate;
    private String createdAt;
    private String coachName;
}