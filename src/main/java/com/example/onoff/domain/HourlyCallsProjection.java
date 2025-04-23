package com.example.onoff.domain;

public interface HourlyCallsProjection {

    Integer getHour();
    Integer getTotalCalls();
    Integer getAnsweredCalls();
}
