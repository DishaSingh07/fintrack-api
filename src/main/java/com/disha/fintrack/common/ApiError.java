package com.disha.fintrack.common;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {
    private String code;
    private String message;
    private Map<String, Object> details;
    private LocalDateTime timestamp;
}