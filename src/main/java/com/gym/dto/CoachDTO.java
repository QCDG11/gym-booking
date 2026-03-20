package com.gym.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachDTO {
    private Long id;
    private Long userId;
    private String name;
    private String avatar;
    private String phone;
    private String specialty;
    private String introduction;
    private Integer yearsOfExperience;
}