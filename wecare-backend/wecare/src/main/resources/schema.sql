-- Test용 DB 및 User 생성 쿼리
-- CREATE DATABASE IF NOT EXISTS wecare_test;
-- CREATE USER IF NOT EXISTS 'wecare_test'@'%' IDENTIFIED BY 'wecareTest';
-- GRANT ALL PRIVILEGES ON wecare_test.* TO 'wecare_test'@'%';
-- USE wecare_test;

-- 1. 사용자 테이블
CREATE TABLE IF NOT EXISTS members (
    id BIGINT  AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50) NOT NULL UNIQUE,
    birth_date TIMESTAMP NOT NULL,
    gender     ENUM('FEMALE', 'MALE') NOT NULL,
    name       VARCHAR(50) NOT NULL,
    password   VARCHAR(100) NOT NULL,
    role       ENUM('DEPENDENT', 'GUARDIAN') NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. 보호자-피보호자 연결 테이블
CREATE TABLE IF NOT EXISTS connections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dependent_id BIGINT NOT NULL,
    guardian_id BIGINT,
    is_active TINYINT NOT NULL DEFAULT 1,
    relationship_type ENUM('PARENT', 'GRANDPARENT', 'SIBLING', 'FRIEND', 'RELATIVE', 'OTHER') NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (dependent_id, guardian_id),
    FOREIGN KEY (dependent_id) REFERENCES members (id) ON DELETE CASCADE,
    FOREIGN KEY (guardian_id) REFERENCES members (id) ON DELETE SET NULL
    -- 연결 해제 시 피보호자의 데이터 보호
);

-- 3. 루틴 테이블
-- 루틴 리소스의 주체는 피보호자가 되어야 함
CREATE TABLE IF NOT EXISTS routines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dependent_id BIGINT NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME,
    title VARCHAR(255) NOT NULL,
    routine_type ENUM('ACTIVITY', 'CUSTOM', 'MEDICATION', 'SUPPLEMENT', 'MEAL') NOT NULL,
    guardian_memo TEXT,
    dependent_memo TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (dependent_id) REFERENCES members (id) ON DELETE CASCADE
);

-- 4. 루틴 반복 요일 테이블
CREATE TABLE IF NOT EXISTS routine_repeat_days (
    id BIGINT PRIMARY KEY,
    routine_id BIGINT NOT NULL,
    repeat_day ENUM('MON','TUE','WED','THU','FRI','SAT','SUN') NOT NULL,
    UNIQUE (routine_id, repeat_day),
    FOREIGN KEY (routine_id) REFERENCES routines (id) ON DELETE CASCADE
);

-- 5. 알림 관련 테이블
CREATE TABLE IF NOT EXISTS routine_alerts (
    routine_id BIGINT PRIMARY KEY,
    is_active TINYINT NOT NULL DEFAULT 1,
    notification_type ENUM('NONE', 'ON_START_TIME', 'ON_END_TIME', 'EVERY_10_MINUTES', 'EVERY_30_MINUTES', 'EVERY_HOUR') NOT NULL,
    sound_type ENUM('DEFAULT_SOUND', 'SILENT', 'VIBRATION', 'VOICE_MESSAGE') NOT NULL,
    voice_message_url VARCHAR(500),
    FOREIGN KEY (routine_id) REFERENCES routines (id) ON DELETE CASCADE
);

-- 6. 루틴 실행 로그
CREATE TABLE IF NOT EXISTS routine_histories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    routine_id BIGINT,
    status ENUM('COMPLETED', 'FAILED') NOT NULL,
    completed_date DATE NOT NULL,
    completed_time TIME NOT NULL,

    UNIQUE(routine_id, completed_date),
    FOREIGN KEY (routine_id) REFERENCES routines (id) ON DELETE SET NULL
);