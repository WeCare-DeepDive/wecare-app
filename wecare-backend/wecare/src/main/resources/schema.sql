-- 1. 사용자 테이블
CREATE TABLE members (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(30) NOT NULL UNIQUE,
                         password VARCHAR(100) NOT NULL,
                         name VARCHAR(50) NOT NULL,
                         gender VARCHAR(10) NOT NULL,
                         role VARCHAR(10) NOT NULL,
                         birth_date DATETIME NOT NULL,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         CHECK (gender IN ('MALE', 'FEMALE')),
                         CHECK (role IN ('GUARDIAN', 'DEPENDENT'))
);

-- 2. 초대 코드 테이블 (주석 처리 - Redis 사용 예정)
-- CREATE TABLE invite_codes (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     code VARCHAR(20) NOT NULL UNIQUE,
--     inviter_id BIGINT NOT NULL,
--     role_target VARCHAR(10) NOT NULL,
--     is_used BOOLEAN DEFAULT FALSE,
--     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
--     expires_at DATETIME,
--     CONSTRAINT fk_invite_codes_inviter FOREIGN KEY (inviter_id) REFERENCES members(id) ON DELETE CASCADE,
--     CHECK (role_target IN ('GUARDIAN', 'DEPENDENT'))
-- );

-- 3. 보호자-피보호자 연결 테이블
CREATE TABLE connections (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             protector_id BIGINT NOT NULL,
                             protected_id BIGINT NOT NULL,
                             connected_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             is_active BOOLEAN DEFAULT TRUE,
                             CONSTRAINT fk_connections_protector FOREIGN KEY (protector_id) REFERENCES members(id) ON DELETE CASCADE,
                             CONSTRAINT fk_connections_protected FOREIGN KEY (protected_id) REFERENCES members(id) ON DELETE CASCADE
);

-- 4. 루틴 테이블
CREATE TABLE routines (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          member_id BIGINT NOT NULL,
                          type VARCHAR(15) NOT NULL,
                          title VARCHAR(100) NOT NULL,
                          description TEXT,
                          disease VARCHAR(100),
                          shared BOOLEAN DEFAULT TRUE,
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_routines_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
                          CHECK (type IN ('MEDICATION', 'SUPPLEMENT', 'CUSTOM'))
);

-- 5. 루틴 체크 테이블
CREATE TABLE routine_checks (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                routine_id BIGINT NOT NULL,
                                member_id BIGINT NOT NULL,
                                checked_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_routine_checks_routine FOREIGN KEY (routine_id) REFERENCES routines(id) ON DELETE CASCADE,
                                CONSTRAINT fk_routine_checks_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);

-- 6. 일정 테이블
CREATE TABLE schedules (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           member_id BIGINT NOT NULL,
                           title VARCHAR(100) NOT NULL,
                           description TEXT,
                           date DATETIME NOT NULL,
                           time TIME,
                           is_shared BOOLEAN DEFAULT TRUE,
                           is_alert_enabled BOOLEAN DEFAULT FALSE,
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_schedules_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);

-- 7. 건강 리포트 테이블
CREATE TABLE health_reports (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                member_id BIGINT NOT NULL,
                                report_date DATETIME NOT NULL,
                                health_score_daily INT,
                                health_score_weekly INT,
                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_health_reports_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);

-- 8. 처방전 테이블
CREATE TABLE prescriptions (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               member_id BIGINT NOT NULL,
                               type VARCHAR(15) NOT NULL,
                               name VARCHAR(100) NOT NULL,
                               method TEXT,
                               start_date DATETIME,
                               end_date DATETIME,
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_prescriptions_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
                               CHECK (type IN ('MEDICATION', 'SUPPLEMENT'))
);

-- 9. 만보계 테이블
CREATE TABLE pedometers (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            member_id BIGINT NOT NULL,
                            date DATETIME NOT NULL,
                            step_count INT DEFAULT 0,
                            distance FLOAT DEFAULT 0,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_pedometers_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);

-- 10. 알림 테이블
CREATE TABLE alerts (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        member_id BIGINT NOT NULL,
                        alert_type VARCHAR(10) NOT NULL,
                        title VARCHAR(100),
                        content TEXT,
                        alert_time DATETIME NOT NULL,
                        repeat_flag BOOLEAN DEFAULT FALSE,
                        sound_type VARCHAR(10) DEFAULT 'DEFAULT',
                        voice_path VARCHAR(255),
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_alerts_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
                        CHECK (alert_type IN ('ROUTINE', 'SCHEDULE', 'CUSTOM')),
                        CHECK (sound_type IN ('DEFAULT', 'CUSTOM'))
);
