-- DB 및 User 생성 쿼리
-- CREATE DATABASE wecare_db;
-- CREATE USER 'wecare_dml'@'%' IDENTIFIED BY 'wecarePassword';
-- GRANT SELECT, DELETE, UPDATE, INSERT ON wecare_db.* TO 'wecare_dml'@'%';
-- USE wecare_db;

-- 1. 사용자 테이블
CREATE TABLE `members`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `birth_date` DATE         NOT NULL,
    `created_at` DATETIME(6) DEFAULT NULL,
    `gender`     ENUM('FEMALE','MALE') NOT NULL,
    `name`       VARCHAR(50)  NOT NULL,
    `password`   VARCHAR(100) NOT NULL,
    `role`       ENUM('DEPENDENT','GUARDIAN') NOT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    `username`   VARCHAR(50)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_members_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 2. 보호자-피보호자 연결 테이블
CREATE TABLE `invitations`
(
    `dependent_id` BIGINT NOT NULL,
    `guardian_id`  BIGINT NOT NULL,
    `created_at`   DATETIME(6) DEFAULT NULL,
    `is_active`    TINYINT(1) NOT NULL DEFAULT '1',
    PRIMARY KEY (`dependent_id`, `guardian_id`),
    KEY            `fk_invitations_guardian_id` (`guardian_id`),
    CONSTRAINT `fk_invitations_dependent_id` FOREIGN KEY (`dependent_id`) REFERENCES `members` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_invitations_guardian_id` FOREIGN KEY (`guardian_id`) REFERENCES `members` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 3. 루틴 테이블
CREATE TABLE `routine`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `completed`    BIT(1)       NOT NULL,
    `created_at`   DATETIME(6) DEFAULT NULL,
    `description`  VARCHAR(1000) DEFAULT NULL,
    `end_time`     DATETIME(6) DEFAULT NULL,
    `is_repeat`    BIT(1)       NOT NULL,
    `start_time`   DATETIME(6) NOT NULL,
    `title`        VARCHAR(255) NOT NULL,
    `type`         ENUM('ACTIVITY','CUSTOM','MEDICATION','SUPPLEMENT') NOT NULL,
    `updated_at`   DATETIME(6) DEFAULT NULL,
    `dependent_id` BIGINT       NOT NULL,
    `guardian_id`  BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    KEY            `fk_routine_dependent_id` (`dependent_id`),
    KEY            `fk_routine_guardian_id` (`guardian_id`),
    CONSTRAINT `fk_routine_dependent_id` FOREIGN KEY (`dependent_id`) REFERENCES `members` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_routine_guardian_id` FOREIGN KEY (`guardian_id`) REFERENCES `members` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 4. 루틴 알람 설정 테이블
CREATE TABLE `routine_alarm_setting`
(
    `id`                     BIGINT NOT NULL AUTO_INCREMENT,
    `alert_before_end_min`   INT    DEFAULT NULL,
    `alert_before_start_min` INT    DEFAULT NULL,
    `repeat_interval_min`    INT    DEFAULT NULL,
    `routine_id`             BIGINT DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_routine_alarm_setting_routine_id` (`routine_id`),
    CONSTRAINT `fk_routine_alarm_setting_routine_id` FOREIGN KEY (`routine_id`) REFERENCES `routine` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 5. 루틴 반복 요일 테이블
CREATE TABLE `routine_repeat_days`
(
    `routine_id` BIGINT NOT NULL,
    `day`        ENUM('DAILY','MON','TUE','WED','THU','FRI','SAT','SUN') NOT NULL,
    UNIQUE KEY `pk_routine_repeat_days` (`routine_id`, `day`),
    CONSTRAINT `fk_routine_repeat_days_routine_id` FOREIGN KEY (`routine_id`) REFERENCES `routine` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 6. 루틴 체크 테이블
CREATE TABLE routine_checks
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    routine_id BIGINT NOT NULL,
    member_id  BIGINT NOT NULL,
    checked_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_routine_checks_routine FOREIGN KEY (routine_id) REFERENCES routine (id) ON DELETE CASCADE,
    CONSTRAINT fk_routine_checks_member FOREIGN KEY (member_id) REFERENCES members (id) ON DELETE CASCADE
);

-- 7. 일정 테이블
CREATE TABLE schedules
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id        BIGINT       NOT NULL,
    title            VARCHAR(100) NOT NULL,
    description      TEXT,
    date             DATETIME     NOT NULL,
    time             TIME,
    is_shared        BOOLEAN  DEFAULT TRUE,
    is_alert_enabled BOOLEAN  DEFAULT FALSE,
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_schedules_member FOREIGN KEY (member_id) REFERENCES members (id) ON DELETE CASCADE
);

-- 8. 건강 리포트 테이블
CREATE TABLE health_reports
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id           BIGINT   NOT NULL,
    report_date         DATETIME NOT NULL,
    health_score_daily  INT,
    health_score_weekly INT,
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_health_reports_member FOREIGN KEY (member_id) REFERENCES members (id) ON DELETE CASCADE
);

-- 9. 처방전 테이블
CREATE TABLE prescriptions
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT       NOT NULL,
    type       ENUM('MEDICATION', 'SUPPLEMENT') NOT NULL,
    name       VARCHAR(100) NOT NULL,
    method     TEXT,
    start_date DATETIME,
    end_date   DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_prescriptions_member FOREIGN KEY (member_id) REFERENCES members (id) ON DELETE CASCADE
);

-- 10. 만보계 테이블
CREATE TABLE pedometers
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT   NOT NULL,
    date       DATETIME NOT NULL,
    step_count INT      DEFAULT 0,
    distance   FLOAT    DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pedometers_member FOREIGN KEY (member_id) REFERENCES members (id) ON DELETE CASCADE
);

-- 11. 알림 테이블
CREATE TABLE alerts
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT   NOT NULL,
    alert_type  ENUM('ROUTINE', 'SCHEDULE', 'CUSTOM') NOT NULL,
    title       VARCHAR(100),
    content     TEXT,
    alert_time  DATETIME NOT NULL,
    repeat_flag BOOLEAN  DEFAULT FALSE,
    sound_type  ENUM('DEFAULT', 'CUSTOM') DEFAULT 'DEFAULT',
    voice_path  VARCHAR(255),
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_alerts_member FOREIGN KEY (member_id) REFERENCES members (id) ON DELETE CASCADE
);