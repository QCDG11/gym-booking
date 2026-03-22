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
    
    private Long coachId;
    private Long userId;
    
    @Column(nullable = false)
    private String type;
    
    private Integer duration;
    private BigDecimal price;
    private Integer totalSessions;
    private Integer remainingSessions;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainingStatus status;
    
    private LocalDateTime expireDate;
    
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