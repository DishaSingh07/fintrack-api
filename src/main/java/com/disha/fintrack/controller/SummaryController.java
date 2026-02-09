package com.disha.fintrack.controller;

import com.disha.fintrack.common.ApiResponse;
import com.disha.fintrack.dto.SummaryResposne;
import com.disha.fintrack.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/summary")
@CrossOrigin("*")
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping
    public ResponseEntity<ApiResponse<SummaryResposne>> getSummary(
            @RequestParam int month,
            @RequestParam int year) {

        SummaryResposne summary = summaryService.generateMonthlySummary(month, year);
        return ResponseEntity.ok(ApiResponse.success(summary));
    }
}
