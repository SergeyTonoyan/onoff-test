package com.example.onoff.service;

import com.example.onoff.domain.HourlyCallsProjection;
import com.example.onoff.dto.HeatMapHourDto;
import com.example.onoff.model.HourlyCallsMock;
import com.example.onoff.repository.CallLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CallLogServiceTest {

    @Mock
    CallLogRepository callLogRepository;

    private CallLogService callLogService;

    @BeforeEach
    void setUp() {
        this.callLogService = new CallLogService(callLogRepository);
    }

    @Test
    public void getAnswerRateReturnsCorrectShades() {

        List<HourlyCallsProjection> testData = List.of(
            new HourlyCallsMock(2, 9, 3), //shade1
            new HourlyCallsMock(3, 9, 6), //shade2
            new HourlyCallsMock(4, 100, 100) //shade3
        );

        when(callLogRepository.findCallsPerHour(any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(testData);

        List<HeatMapHourDto> result = callLogService.getAnswerRate(LocalDate.of(2020,9,13), 3, 2, 4);
        List<String> shades = result.stream()
                .map(HeatMapHourDto::getShade)
                .toList();

        assertEquals(List.of("Shade1","Shade2","Shade3"), shades);
    }

    @Test
    public void getAnswerRateReturnsCorrectDto() {

        List<HourlyCallsProjection> testData = List.of(
                new HourlyCallsMock(2, 9, 6)
        );

        when(callLogRepository.findCallsPerHour(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(testData);

        List<HeatMapHourDto> result = callLogService.getAnswerRate(LocalDate.of(2020,9,13), 3, 2, 3);
        assertEquals(2, result.size());
        HeatMapHourDto dto0 = result.get(0);
        assertEquals(9, dto0.getTotalCalls());
        assertEquals(6, dto0.getAnsweredCalls());
        assertEquals(66.667f, dto0.getRate(),0.0001f);
        assertEquals("Shade2", dto0.getShade());

        HeatMapHourDto dto1 = result.get(1);
        assertEquals(0, dto1.getTotalCalls());
        assertEquals(0, dto1.getAnsweredCalls());
        assertEquals(0f, dto1.getRate());
        assertEquals("Shade1", dto1.getShade());
    }

    @Test
    public void getAnswerRateThrowsExceptionIfRangeIsInvalid() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> callLogService.getAnswerRate(LocalDate.of(2020,9,13), 4, 5, 4)
        );
        assertEquals("End hour must be greater than or equal to start hour", ex.getMessage());
    }

    @Test
    public void getAnswerRateReturnsAllZeroDtosIfNoDataInDB() {

        when(callLogRepository.findCallsPerHour(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        List<HeatMapHourDto> result = callLogService.getAnswerRate(LocalDate.of(2020,9,13), 3, 2, 4);
        assertEquals(3, result.size());
        for (HeatMapHourDto dto : result) {
            assertEquals(0, dto.getTotalCalls());
            assertEquals(0, dto.getAnsweredCalls());
            assertEquals(0f, dto.getRate());
            assertEquals("Shade1", dto.getShade());
        }
    }

    @Test
    void getAnswerRateCallRepositoryWithCorrectDates() {
        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor   = ArgumentCaptor.forClass(LocalDateTime.class);
        when(callLogRepository.findCallsPerHour(startCaptor.capture(), endCaptor.capture()))
                .thenReturn(Collections.emptyList());
        LocalDate date = LocalDate.of(2020,9,13);
        this.callLogService.getAnswerRate(date, 5, 8, 12);
        LocalDateTime expectedStart = LocalDateTime.of(date, LocalTime.of(8, 0));
        LocalDateTime expectedEnd   = LocalDateTime.of(date, LocalTime.of(12, 59, 59, 999_000_000));
        assertEquals(expectedStart, startCaptor.getValue());
        assertEquals(expectedEnd,   endCaptor.getValue());
    }
}
