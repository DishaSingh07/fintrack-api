package com.disha.fintrack.controller;

import com.disha.fintrack.common.ApiResponse;
import com.disha.fintrack.dto.Period;
import com.disha.fintrack.service.PeriodWiseAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics/period-wise")
@CrossOrigin("*")
public class PeriodWiseAnalyticsController {

    private final PeriodWiseAnalyticsService periodWiseAnalyticsService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getPeriodWiseAnalytics(
            @RequestParam int month,
            @RequestParam int year) {

        Period period = new Period(month, year);
        Object analytics = periodWiseAnalyticsService.getPeriodWiseAnalytics(period);
        return ResponseEntity.ok(ApiResponse.success(analytics));
    }
}