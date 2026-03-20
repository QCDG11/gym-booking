package com.gym.service;

import com.gym.dto.*;
import com.gym.entity.User;
import com.gym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : "MEMBER")
                .nickname(request.getNickname())
                .phone(request.getPhone())
                .enabled(true)
                .build();
        
        return userRepository.save(user);
    }
    
    public boolean validatePassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
    
    @Transactional
    public User updateProfile(Long userId, UserDTO dto) {
        User user = findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (dto.getNickname() != null) user.setNickname(dto.getNickname());
        if (dto.getAvatar() != null) user.setAvatar(dto.getAvatar());
        if (dto.getGender() != null) user.setGender(dto.getGender());
        if (dto.getBirthday() != null) user.setBirthday(dto.getBirthday());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        
        return userRepository.save(user);
    }
    
    @Transactional
    public void setPayPassword(Long userId, String payPassword) {
        User user = findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPayPassword(passwordEncoder.encode(payPassword));
        userRepository.save(user);
    }
    
    public boolean validatePayPassword(Long userId, String rawPayPassword) {
        User user = findById(userId);
        if (user == null || user.getPayPassword() == null) {
            return false;
        }
        return passwordEncoder.matches(rawPayPassword, user.getPayPassword());
    }
}