package com.gym.config;

import com.gym.entity.*;
import com.gym.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final CourseRepository courseRepository;
    private final CoachRepository coachRepository;
    private final CourseScheduleRepository scheduleRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            log.info("初始化数据...");
            
            // 初始化课程
            if (courseRepository.count() == 0) {
                initCourses();
            }
            
            // 初始化教练
            if (coachRepository.count() == 0) {
                initCoaches();
            }
            
            // 初始化课程安排
            if (scheduleRepository.count() == 0) {
                initSchedules();
            }
            
            log.info("数据初始化完成");
        };
    }
    
    private void initCourses() {
        courseRepository.save(Course.builder()
                .name("瑜伽课")
                .description("放松身心，提升柔韧性")
                .category("瑜伽")
                .duration(60)
                .maxParticipants(20)
                .price(new BigDecimal("99"))
                .imageUrl("/images/course-yoga.jpg")
                .enabled(true)
                .build());
        
        courseRepository.save(Course.builder()
                .name("动感单车")
                .description("燃脂塑形，增强心肺功能")
                .category("动感单车")
                .duration(45)
                .maxParticipants(20)
                .price(new BigDecimal("79"))
                .imageUrl("/images/course-spin.jpg")
                .enabled(true)
                .build());
        
        courseRepository.save(Course.builder()
                .name("普拉提")
                .description("核心训练，改善体态")
                .category("普拉提")
                .duration(60)
                .maxParticipants(15)
                .price(new BigDecimal("119"))
                .imageUrl("/images/course-pilates.jpg")
                .enabled(true)
                .build());
        
        courseRepository.save(Course.builder()
                .name("力量训练")
                .description("增肌塑形，增强力量")
                .category("力量训练")
                .duration(60)
                .maxParticipants(15)
                .price(new BigDecimal("89"))
                .imageUrl("/images/course-strength.jpg")
                .enabled(true)
                .build());
        
        courseRepository.save(Course.builder()
                .name("拳击课")
                .description("燃脂防身，释放压力")
                .category("拳击")
                .duration(60)
                .maxParticipants(20)
                .price(new BigDecimal("109"))
                .imageUrl("/images/course-boxing.jpg")
                .enabled(true)
                .build());
        
        log.info("已初始化 {} 个课程", courseRepository.count());
    }
    
    private void initCoaches() {
        coachRepository.save(Coach.builder()
                .name("张教练")
                .avatar("/images/coach1.jpg")
                .phone("13800138001")
                .specialty("瑜伽、普拉提")
                .introduction("资深瑜伽教练，8年教学经验，擅长理疗瑜伽")
                .yearsOfExperience(8)
                .enabled(true)
                .build());
        
        coachRepository.save(Coach.builder()
                .name("李教练")
                .avatar("/images/coach2.jpg")
                .phone("13800138002")
                .specialty("动感单车、力量训练")
                .introduction("国家二级运动员，擅长减脂塑形")
                .yearsOfExperience(5)
                .enabled(true)
                .build());
        
        coachRepository.save(Coach.builder()
                .name("王教练")
                .avatar("/images/coach3.jpg")
                .phone("13800138003")
                .specialty("拳击、散打")
                .description("前省队拳击运动员，专业拳击教练")
                .yearsOfExperience(10)
                .enabled(true)
                .build());
        
        log.info("已初始化 {} 个教练", coachRepository.count());
    }
    
    private void initSchedules() {
        var courses = courseRepository.findByEnabledTrue();
        var coaches = coachRepository.findByEnabledTrue();
        
        if (courses.isEmpty() || coaches.isEmpty()) {
            log.warn("课程或教练数据为空，跳过课表初始化");
            return;
        }
        
        // 今天的课程安排
        LocalDateTime now = LocalDateTime.now();
        
        // 瑜伽课安排
        scheduleRepository.save(CourseSchedule.builder()
                .courseId(courses.get(0).getId())
                .courseName(courses.get(0).getName())
                .coachId(coaches.get(0).getId())
                .coachName(coaches.get(0).getName())
                .startTime(now.plusHours(2))
                .endTime(now.plusHours(3))
                .location("瑜伽教室A")
                .maxParticipants(20)
                .currentParticipants(0)
                .enabled(true)
                .build());
        
        // 动感单车安排
        scheduleRepository.save(CourseSchedule.builder()
                .courseId(courses.get(1).getId())
                .courseName(courses.get(1).getName())
                .coachId(coaches.get(1).getId())
                .coachName(coaches.get(1).getName())
                .startTime(now.plusHours(4))
                .endTime(now.plusHours(5))
                .location("动感单车教室")
                .maxParticipants(20)
                .currentParticipants(0)
                .enabled(true)
                .build());
        
        // 普拉提安排
        scheduleRepository.save(CourseSchedule.builder()
                .courseId(courses.get(2).getId())
                .courseName(courses.get(2).getName())
                .coachId(coaches.get(0).getId())
                .coachName(coaches.get(0).getName())
                .startTime(now.plusDays(1).withHour(10).withMinute(0))
                .endTime(now.plusDays(1).withHour(11).withMinute(0))
                .location("普拉提教室")
                .maxParticipants(15)
                .currentParticipants(0)
                .enabled(true)
                .build());
        
        // 力量训练安排
        scheduleRepository.save(CourseSchedule.builder()
                .courseId(courses.get(3).getId())
                .courseName(courses.get(3).getName())
                .coachId(coaches.get(1).getId())
                .coachName(coaches.get(1).getName())
                .startTime(now.plusDays(1).withHour(14).withMinute(0))
                .endTime(now.plusDays(1).withHour(15).withMinute(0))
                .location("力量训练区")
                .maxParticipants(15)
                .currentParticipants(0)
                .enabled(true)
                .build());
        
        // 拳击课安排
        scheduleRepository.save(CourseSchedule.builder()
                .courseId(courses.get(4).getId())
                .courseName(courses.get(4).getName())
                .coachId(coaches.get(2).getId())
                .coachName(coaches.get(2).getName())
                .startTime(now.plusDays(1).withHour(16).withMinute(0))
                .endTime(now.plusDays(1).withHour(17).withMinute(0))
                .location("拳击台")
                .maxParticipants(20)
                .currentParticipants(0)
                .enabled(true)
                .build());
        
        log.info("已初始化 {} 个课程安排", scheduleRepository.count());
    }
}