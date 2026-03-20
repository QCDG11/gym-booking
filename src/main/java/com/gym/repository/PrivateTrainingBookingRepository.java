package com.gym.repository;

import com.gym.entity.PrivateTrainingBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrivateTrainingBookingRepository extends JpaRepository<PrivateTrainingBooking, Long> {
    List<PrivateTrainingBooking> findByPrivateTrainingId(Long privateTrainingId);
    List<PrivateTrainingBooking> findByPrivateTrainingUserId(Long userId);
}