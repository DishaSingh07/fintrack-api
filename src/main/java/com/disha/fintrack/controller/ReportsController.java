package com.disha.fintrack.controller;

import com.disha.fintrack.common.ApiResponse;
import com.disha.fintrack.service.ExpenseService;
import com.disha.fintrack.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/reports")
@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReportsController {

    private final ExpenseService expenseService;
    private final ReportService reportService;

    @GetMapping("/expense-breakdown")
    public ResponseEntity<ApiResponse<?>> getExpenseBreakdown(@RequestParam int month, @RequestParam int year) {
        return ResponseEntity.ok(ApiResponse.success(expenseService.getExpenseBreakdown(month, year)));
    }

    @GetMapping("/trends")
    public ResponseEntity<ApiResponse<?>> getMonthlyTrends(@RequestParam(defaultValue = "6") int months) {
        if (months > 12) {
            months = 12;
        }
        return ResponseEntity.ok(ApiResponse.success(reportService.getMonthlyTrends(months)));
    }

}
