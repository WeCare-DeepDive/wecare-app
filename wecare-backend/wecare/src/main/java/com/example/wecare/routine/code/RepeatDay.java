package com.example.wecare.routine.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;

@Getter
@RequiredArgsConstructor
public enum RepeatDay {
    MON(DayOfWeek.MONDAY),
    TUE(DayOfWeek.TUESDAY),
    WED(DayOfWeek.WEDNESDAY),
    THU(DayOfWeek.THURSDAY),
    FRI(DayOfWeek.FRIDAY),
    SAT(DayOfWeek.SATURDAY),
    SUN(DayOfWeek.SUNDAY);

    private final DayOfWeek dayOfWeek;
}