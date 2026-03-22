package com.gym.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long scheduleId;
    private Long userId;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime bookedAt;
    
    private LocalDateTime cancelledAt;
    
    @PrePersist
    protected void onCreate() {
        bookedAt = LocalDateTime.now();
        status = BookingStatus.CONFIRMED;
    }
    
    public enum BookingStatus {
        CONFIRMED, CANCELLED, COMPLETED
    }
}