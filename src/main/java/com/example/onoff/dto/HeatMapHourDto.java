package com.example.onoff.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HeatMapHourDto {

    private final int hour;
    private final int answeredCalls;
    private final int totalCalls;
    private final float rate;
    private final String shade;
}
