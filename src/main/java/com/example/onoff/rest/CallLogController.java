package com.example.onoff.rest;

import com.example.onoff.dto.HeatMapHourDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/heatmap")
@RequiredArgsConstructor
public class CallLogController {

    @GetMapping("/answer-rate")
    public List<HeatMapHourDto> getAnswerRate() {
        return new ArrayList<>();
    }
}
