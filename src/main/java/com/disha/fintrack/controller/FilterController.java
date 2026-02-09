package com.disha.fintrack.controller;

import com.disha.fintrack.common.ApiResponse;
import com.disha.fintrack.dto.FilterDTO;
import com.disha.fintrack.service.ExpenseService;
import com.disha.fintrack.service.FilterService;
import com.disha.fintrack.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
@CrossOrigin("*")
public class FilterController {

    private final FilterService filterService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> filterTransactions(@RequestBody FilterDTO filter) {
        Object result = filterService.filterTransactions(filter);
        return ResponseEntity.ok(ApiResponse.success(result));
    }


}