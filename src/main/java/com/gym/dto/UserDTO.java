package com.gym.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String role;
    private String nickname;
    private String avatar;
    private String gender;
    private String birthday;
    private String phone;
}