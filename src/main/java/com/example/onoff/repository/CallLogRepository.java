package com.example.onoff.repository;

import com.example.onoff.domain.CallLog;
import com.example.onoff.domain.HourlyCallsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CallLogRepository extends JpaRepository<CallLog, Long> {

    @Query(value =
        """
            SELECT
               FUNCTION('HOUR', c.startedAt) AS hour,
               COUNT(*) AS totalCalls,
               SUM(CASE WHEN c.status = 'ANSWERED' THEN 1 ELSE 0 END) AS answeredCalls
            FROM CallLog c
            WHERE c.startedAt BETWEEN :start AND :end
            GROUP BY FUNCTION('HOUR', c.startedAt)
        """)
    List<HourlyCallsProjection> findCallsPerHour(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
