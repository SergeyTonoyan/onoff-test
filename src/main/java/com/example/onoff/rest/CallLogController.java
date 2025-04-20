package com.example.onoff.rest;

import com.example.onoff.dto.HeatMapHourDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/heatmap")
@RequiredArgsConstructor
@Validated
public class CallLogController {

    @GetMapping("/answer-rate")
    public List<HeatMapHourDto> getAnswerRate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateInput,
        @RequestParam @Min(3) @Max(10) int numberOfShades,
        @RequestParam(defaultValue="0") @Min(0) @Max(23) int startHour,
        @RequestParam(defaultValue="23") @Min(0) @Max(23) int endHour
    ) {
        return new ArrayList<>();
    }
}
