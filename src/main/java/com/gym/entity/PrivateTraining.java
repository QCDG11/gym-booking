package com.gym.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "private_trainings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivateTraining {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String type; // 私教类型
    
    private Integer duration; // 时长（分钟）
    private BigDecimal price; // 价格
    private Integer totalSessions; // 总次数
    private Integer remainingSessions; // 剩余次数
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainingStatus status; // ACTIVE, COMPLETED, EXPIRED
    
    private LocalDateTime expireDate; // 过期日期
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = TrainingStatus.ACTIVE;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum TrainingStatus {
        ACTIVE, COMPLETED, EXPIRED
    }
}