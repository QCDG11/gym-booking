package com.gym.service;

import com.gym.dto.CoachDTO;
import com.gym.entity.Coach;
import com.gym.entity.User;
import com.gym.repository.CoachRepository;
import com.gym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachService {
    
    private final CoachRepository coachRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public List<Coach> findAll() {
        return coachRepository.findByEnabledTrue();
    }
    
    public Coach findById(Long id) {
        return coachRepository.findById(id).orElse(null);
    }
    
    public Coach findByUserId(Long userId) {
        // 暂时返回 null，因为不再关联 User
        return null;
    }
    
    @Transactional
    public Coach createCoach(CoachDTO dto, String username, String password) {
        // 创建教练信息
        Coach coach = Coach.builder()
                .name(dto.getName())
                .avatar(dto.getAvatar())
                .phone(dto.getPhone())
                .specialty(dto.getSpecialty())
                .introduction(dto.getIntroduction())
                .yearsOfExperience(dto.getYearsOfExperience())
                .enabled(true)
                .build();
        
        return coachRepository.save(coach);
    }
    
    @Transactional
    public Coach updateCoach(Long id, CoachDTO dto) {
        Coach coach = findById(id);
        if (coach == null) throw new RuntimeException("教练不存在");
        
        if (dto.getName() != null) coach.setName(dto.getName());
        if (dto.getAvatar() != null) coach.setAvatar(dto.getAvatar());
        if (dto.getPhone() != null) coach.setPhone(dto.getPhone());
        if (dto.getSpecialty() != null) coach.setSpecialty(dto.getSpecialty());
        if (dto.getIntroduction() != null) coach.setIntroduction(dto.getIntroduction());
        if (dto.getYearsOfExperience() != null) coach.setYearsOfExperience(dto.getYearsOfExperience());
        
        return coachRepository.save(coach);
    }
    
    @Transactional
    public void deleteCoach(Long id) {
        Coach coach = findById(id);
        if (coach != null) {
            coach.setEnabled(false);
            coachRepository.save(coach);
        }
    }
}