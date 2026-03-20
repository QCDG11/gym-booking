package com.gym.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer duration;
    private Integer maxParticipants;
    private BigDecimal price;
    private String imageUrl;
}