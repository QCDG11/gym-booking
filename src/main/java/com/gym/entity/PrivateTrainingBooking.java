package com.gym.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "private_training_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivateTrainingBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "private_training_id", nullable = false)
    private PrivateTraining privateTraining;
    
    @Column(nullable = false)
    private LocalDateTime bookedTime; // 预约时间
    
    private String location; // 地点
    private String note; // 备注
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status; // CONFIRMED, CANCELLED, COMPLETED
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime cancelledAt;
    private LocalDateTime completedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = BookingStatus.CONFIRMED;
    }
    
    public enum BookingStatus {
        CONFIRMED, CANCELLED, COMPLETED
    }
}