package com.example.onoff.service;

import com.example.onoff.domain.HourlyCallsProjection;
import com.example.onoff.dto.HeatMapHourDto;
import com.example.onoff.repository.CallLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CallLogService {

    private final CallLogRepository callLogRepository;

    public List<HeatMapHourDto> getAnswerRate(LocalDate date, int numberOfShades, int startHour, int endHour) {

        if (endHour < startHour) {
            throw new IllegalArgumentException("End hour must be greater than or equal to start hour");
        }
        LocalDateTime start = date.atTime(startHour, 0);
        LocalDateTime end = date.atTime(endHour, 59, 59, 999_000_000);
        List<HourlyCallsProjection> hourlyCalls = this.callLogRepository.findCallsPerHour(start, end);

        Map<Integer, HourlyCallsProjection> hourlyCallsMap = hourlyCalls.stream()
            .collect(Collectors.toMap(
                    HourlyCallsProjection::getHour,
                    Function.identity()
            ));

        return IntStream.rangeClosed(startHour, endHour).mapToObj(hour -> {
            HourlyCallsProjection hourlyCallsProjection = hourlyCallsMap.get(hour);
            int total = hourlyCallsProjection != null ? hourlyCallsProjection.getTotalCalls() : 0;
            int answered = hourlyCallsProjection != null ? hourlyCallsProjection.getAnsweredCalls() : 0;
            float rateRaw = total > 0 ? (float) answered / total * 100f : 0;
            float rate = BigDecimal.valueOf(rateRaw).setScale(3, RoundingMode.HALF_UP).floatValue();
            int currentShadeNumber = calculateShadeNumber(total, answered, numberOfShades);
            return new HeatMapHourDto(hour, answered, total, rate, "Shade" + currentShadeNumber);
        })
        .toList();
    }

    private int calculateShadeNumber(int total, int answered, int numberOfShades) {

        if (total == 0) {
            return 1;
        }
        return IntStream.rangeClosed(1, numberOfShades)
            .filter(j ->
                answered * numberOfShades <= total * j
            )
            .findFirst()
            .orElse(numberOfShades);
    }
}
