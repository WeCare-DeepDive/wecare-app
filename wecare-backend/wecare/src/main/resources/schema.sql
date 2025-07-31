-- Test용 DB 및 User 생성 쿼리
-- CREATE DATABASE IF NOT EXISTS wecare_test;
-- CREATE USER IF NOT EXISTS 'wecare_test'@'%' IDENTIFIED BY 'wecareTest';
-- GRANT ALL PRIVILEGES ON wecare_test.* TO 'wecare_test'@'%';
-- USE wecare_test;

-- 1. 사용자 테이블
CREATE TABLE IF NOT EXISTS members (
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    birth_date DATE NOT NULL,
    created_at DATETIME(6),
    gender     ENUM('FEMALE', 'MALE') NOT NULL,
    name       VARCHAR(50) NOT NULL,
    password   VARCHAR(100) NOT NULL,
    role       ENUM('DEPENDENT', 'GUARDIAN') NOT NULL,
    updated_at DATETIME(6),
    username   VARCHAR(50) NOT NULL UNIQUE
);

-- 2. 보호자-피보호자 연결 테이블
CREATE TABLE IF NOT EXISTS invitations (
    dependent_id      BIGINT NOT NULL,
    guardian_id       BIGINT NOT NULL,
    created_at        DATETIME(6),
    is_active         TINYINT(1) NOT NULL DEFAULT 1,
    relationship_type ENUM('PARENT', 'GRANDPARENT', 'SIBLING', 'FRIEND', 'RELATIVE', 'OTHER') NOT NULL,
    PRIMARY KEY (dependent_id, guardian_id),
    FOREIGN KEY (dependent_id) REFERENCES members (id) ON DELETE CASCADE,
    FOREIGN KEY (guardian_id) REFERENCES members (id) ON DELETE CASCADE
);

-- 3. 루틴 테이블
CREATE TABLE IF NOT EXISTS routine (
    id                BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    completed_at      DATETIME(6) DEFAULT NULL,
    created_at        DATETIME(6) NOT NULL,
    end_time          DATETIME(6),
    is_repeat         TINYINT(1) NOT NULL,
    start_time        DATETIME(6) NOT NULL,
    title             VARCHAR(255) NOT NULL,
    type              ENUM('ACTIVITY', 'CUSTOM', 'MEDICATION', 'SUPPLEMENT', 'MEAL') NOT NULL,
    updated_at        DATETIME(6) NOT NULL,
    dependent_id      BIGINT NOT NULL,
    guardian_id       BIGINT NOT NULL,
    guardian_memo     TEXT,
    dependent_memo    TEXT,

    -- 알림 관련 컬럼
    is_enabled            TINYINT(1) NOT NULL DEFAULT 1,
    notification_type     ENUM('NONE', 'ON_START_TIME', 'ON_END_TIME', 'EVERY_10_MINUTES', 'EVERY_30_MINUTES', 'EVERY_HOUR') DEFAULT NULL,
    sound_type            ENUM('DEFAULT_SOUND', 'SILENT', 'VIBRATION', 'VOICE_MESSAGE') DEFAULT NULL,
    voice_message_url     VARCHAR(500) DEFAULT NULL,

    FOREIGN KEY (dependent_id) REFERENCES members (id) ON DELETE CASCADE,
    FOREIGN KEY (guardian_id) REFERENCES members (id) ON DELETE CASCADE
);

-- 4. 루틴 반복 요일 테이블
CREATE TABLE IF NOT EXISTS routine_repeat_days (
    routine_id BIGINT NOT NULL,
    day        ENUM('DAILY','MON','TUE','WED','THU','FRI','SAT','SUN') NOT NULL,
    PRIMARY KEY (routine_id, day),
    FOREIGN KEY (routine_id) REFERENCES routine (id) ON DELETE CASCADE
);
