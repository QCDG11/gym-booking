package com.gym.repository;

import com.gym.entity.PrivateTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrivateTrainingRepository extends JpaRepository<PrivateTraining, Long> {
    List<PrivateTraining> findByUserId(Long userId);
    List<PrivateTraining> findByCoachId(Long coachId);
    Optional<PrivateTraining> findByUserIdAndStatus(Long userId, PrivateTraining.TrainingStatus status);
    List<PrivateTraining> findByCoachIdAndStatus(Long coachId, PrivateTraining.TrainingStatus status);
}