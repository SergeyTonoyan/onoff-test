package com.example.onoff.model;

import com.example.onoff.domain.HourlyCallsProjection;

public class HourlyCallsMock implements HourlyCallsProjection {

    private final Integer hour;
    private final Integer totalCalls;
    private final Integer answeredCalls;

    public HourlyCallsMock(Integer hour, Integer totalCalls, Integer answeredCalls) {
        this.hour = hour;
        this.totalCalls = totalCalls;
        this.answeredCalls = answeredCalls;
    }

    @Override
    public Integer getHour() {
        return hour;
    }

    @Override
    public Integer getTotalCalls() {
        return totalCalls;
    }

    @Override
    public Integer getAnsweredCalls() {
        return answeredCalls;
    }
}
