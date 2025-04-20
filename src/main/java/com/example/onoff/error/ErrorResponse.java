package com.example.onoff.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorResponse {

    private static final String DEFAULT_ERROR_CODE = "error.tech";

    @Builder.Default
    private String errorCode = DEFAULT_ERROR_CODE;
    @Builder.Default
    private String message = "";
    @Builder.Default
    private List<Map<String, Object>> details = Collections.emptyList();
    @Builder.Default
    private String path = "";
    @Builder.Default
    private int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
}
