package com.example.wecare.routine.domain;

public enum NotificationType {
    NONE,               // 알림 없음
    ON_START_TIME,      // 시작 시간에 알림
    ON_END_TIME,        // 종료 시간에 알림
    EVERY_10_MINUTES,   // 10분마다
    EVERY_30_MINUTES,   // 30분마다
    EVERY_HOUR          // 1시간마다
}
