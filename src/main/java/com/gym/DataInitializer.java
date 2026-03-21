package com.gym;

import com.gym.entity.*;
import com.gym.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final CoachRepository coachRepository;
    private final CourseScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (courseRepository.count() > 0) {
            log.info("数据已存在，跳过初始化");
            return;
        }

        log.info("开始初始化课程和私教数据...");

        // 创建课程
        Course yoga = courseRepository.save(Course.builder()
                .name("瑜伽课程")
                .description("专业瑜伽教练指导，放松身心")
                .category("瑜伽")
                .duration(60)
                .maxParticipants(20)
                .price(new BigDecimal("68.00"))
                .enabled(true)
                .build());

        Course pilates = courseRepository.save(Course.builder()
                .name("普拉提")
                .description("核心力量训练，塑造完美体型")
                .category("普拉提")
                .duration(45)
                .maxParticipants(15)
                .price(new BigDecimal("88.00"))
                .enabled(true)
                .build());

        Course spinning = courseRepository.save(Course.builder()
                .name("动感单车")
                .description("激情骑行，燃脂首选")
                .category("动感单车")
                .duration(45)
                .maxParticipants(30)
                .price(new BigDecimal("58.00"))
                .enabled(true)
                .build());

        Course boxing = courseRepository.save(Course.builder()
                .name("拳击课")
                .description("释放压力，提升格斗技巧")
                .category("拳击")
                .duration(60)
                .maxParticipants(20)
                .price(new BigDecimal("98.00"))
                .enabled(true)
                .build());

        Course strength = courseRepository.save(Course.builder()
                .name("力量训练")
                .description("器械训练，增强肌肉力量")
                .category("健身")
                .duration(60)
                .maxParticipants(15)
                .price(new BigDecimal("78.00"))
                .enabled(true)
                .build());

        Course dance = courseRepository.save(Course.builder()
                .name("舞蹈课程")
                .description("Jazz、Hiphop等多种舞蹈风格")
                .category("舞蹈")
                .duration(60)
                .maxParticipants(25)
                .price(new BigDecimal("75.00"))
                .enabled(true)
                .build());

        // 创建课程安排
        LocalDateTime now = LocalDateTime.now();
        scheduleRepository.save(CourseSchedule.builder()
                .course(yoga)
                .startTime(now.plusHours(2))
                .endTime(now.plusHours(3))
                .location("瑜伽室A")
                .currentParticipants(0)
                .enabled(true)
                .build());

        scheduleRepository.save(CourseSchedule.builder()
                .course(pilates)
                .startTime(now.plusHours(4))
                .endTime(now.plusHours(5))
                .location("普拉提室")
                .currentParticipants(0)
                .enabled(true)
                .build());

        scheduleRepository.save(CourseSchedule.builder()
                .course(spinning)
                .startTime(now.plusHours(6))
                .endTime(now.plusHours(7))
                .location("动感单车房")
                .currentParticipants(0)
                .enabled(true)
                .build());

        scheduleRepository.save(CourseSchedule.builder()
                .course(boxing)
                .startTime(now.plusHours(8))
                .endTime(now.plusHours(9))
                .location("拳击台")
                .currentParticipants(0)
                .enabled(true)
                .build());

        // 创建教练用户
        User coach1 = userRepository.save(User.builder()
                .username("coach_zhang")
                .password("$2a$10$N9qo8uLOickgx2ZMRZoMye/AgcO5z7j7Z5rQj1Jv7vK1yXqGvLQ2i") // password123
                .email("zhang@gym.com")
                .role("COACH")
                .enabled(true)
                .build());

        User coach2 = userRepository.save(User.builder()
                .username("coach_li")
                .password("$2a$10$N9qo8uLOickgx2ZMRZoMye/AgcO5z7j7Z5rQj1Jv7vK1yXqGvLQ2i")
                .email("li@gym.com")
                .role("COACH")
                .enabled(true)
                .build());

        User coach3 = userRepository.save(User.builder()
                .username("coach_wang")
                .password("$2a$10$N9qo8uLOickgx2ZMRZoMye/AgcO5z7j7Z5rQj1Jv7vK1yXqGvLQ2i")
                .email("wang@gym.com")
                .role("COACH")
                .enabled(true)
                .build());

        // 创建私教
        coachRepository.save(Coach.builder()
                .user(coach1)
                .name("张教练")
                .phone("13800138001")
                .specialty("瑜伽、普拉提")
                .introduction("资深瑜伽教练，8年教学经验，持有RYT200认证")
                .yearsOfExperience(8)
                .enabled(true)
                .build());

        coachRepository.save(Coach.builder()
                .user(coach2)
                .name("李教练")
                .phone("13800138002")
                .specialty("力量训练、增肌")
                .introduction("国家职业健身教练，擅长增肌减脂，NBA体能训练背景")
                .yearsOfExperience(6)
                .enabled(true)
                .build());

        coachRepository.save(Coach.builder()
                .user(coach3)
                .name("王教练")
                .phone("13800138003")
                .specialty("拳击、格斗")
                .introduction("职业拳击手退役，曾获省级比赛冠军，擅长搏击教学")
                .yearsOfExperience(10)
                .enabled(true)
                .build());

        log.info("数据初始化完成！");
    }
}