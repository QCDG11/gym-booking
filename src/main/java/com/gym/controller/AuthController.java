package com.gym.controller;

import com.gym.config.JwtTokenProvider;
import com.gym.dto.*;
import com.gym.entity.User;
import com.gym.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null || !user.getEnabled()) {
            return ResponseEntity.badRequest().body(Map.of("message", "用户名或密码错误"));
        }
        
        if (!userService.validatePassword(user, request.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message", "用户名或密码错误"));
        }
        
        String token = tokenProvider.generateToken(user.getUsername(), user.getRole(), user.getId());
        
        LoginResponse response = LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .userId(user.getId())
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            String token = tokenProvider.generateToken(user.getUsername(), user.getRole(), user.getId());
            
            LoginResponse response = LoginResponse.builder()
                    .token(token)
                    .username(user.getUsername())
                    .role(user.getRole())
                    .userId(user.getId())
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        UserDTO dto = UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .phone(user.getPhone())
                .build();
        
        return ResponseEntity.ok(dto);
    }
    
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal User user, @RequestBody UserDTO dto) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        User updated = userService.updateProfile(user.getId(), dto);
        
        UserDTO response = UserDTO.builder()
                .id(updated.getId())
                .username(updated.getUsername())
                .role(updated.getRole())
                .nickname(updated.getNickname())
                .avatar(updated.getAvatar())
                .gender(updated.getGender())
                .birthday(updated.getBirthday())
                .phone(updated.getPhone())
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/pay-password")
    public ResponseEntity<?> setPayPassword(@AuthenticationPrincipal User user, @RequestBody Map<String, String> request) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        String payPassword = request.get("payPassword");
        if (payPassword == null || payPassword.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "支付密码不能为空"));
        }
        
        userService.setPayPassword(user.getId(), payPassword);
        return ResponseEntity.ok(Map.of("message", "支付密码设置成功"));
    }
    
    @PostMapping("/verify-pay-password")
    public ResponseEntity<?> verifyPayPassword(@AuthenticationPrincipal User user, @RequestBody Map<String, String> request) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "未登录"));
        }
        
        String payPassword = request.get("payPassword");
        boolean valid = userService.validatePayPassword(user.getId(), payPassword);
        
        return ResponseEntity.ok(Map.of("valid", valid));
    }
}