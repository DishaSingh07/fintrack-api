package com.disha.fintrack.controller;

import com.disha.fintrack.common.ApiResponse;
import com.disha.fintrack.dto.IncomeDTO;
import com.disha.fintrack.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
@CrossOrigin("*")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<ApiResponse<IncomeDTO>> addIncome(@RequestBody IncomeDTO incomeDTO) {
        IncomeDTO saved = incomeService.addIncome(incomeDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(saved, "Income created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IncomeDTO>> getIncomeById(@PathVariable Long id) {
        IncomeDTO income = incomeService.getIncomeById(id);
        return ResponseEntity.ok(ApiResponse.success(income));
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<ApiResponse<List<IncomeDTO>>> getIncomesByProfileId(@PathVariable Long profileId) {
        List<IncomeDTO> incomes = incomeService.getIncomesByProfileId(profileId);
        return ResponseEntity.ok(ApiResponse.success(incomes));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<IncomeDTO>>> getIncomesByCategory(@PathVariable Long categoryId) {
        List<IncomeDTO> incomes = incomeService.getIncomesByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(incomes));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<IncomeDTO>>> getAllUserIncomes() {
        List<IncomeDTO> incomes = incomeService.getAllUserIncomes();
        return ResponseEntity.ok(ApiResponse.success(incomes));
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalIncome() {
        return ResponseEntity.ok(ApiResponse.success(incomeService.getTotalIncomeForCurrentUser()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<IncomeDTO>> updateIncome(@PathVariable Long id,
                                                               @RequestBody IncomeDTO incomeDTO) {
        IncomeDTO updated = incomeService.updateIncome(id, incomeDTO);
        return ResponseEntity.ok(ApiResponse.success(updated, "Income updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Income deleted successfully"));
    }

    @GetMapping("/current-month")
    public ResponseEntity<ApiResponse<List<IncomeDTO>>> getCurrentMonthIncomes() {
        List<IncomeDTO> incomes = incomeService.getCurrentMonthIncomesForCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(incomes));
    }

    @GetMapping("/month")
    public ResponseEntity<ApiResponse<List<IncomeDTO>>> getIncomeForMonth(@RequestParam int month,
                                                                          @RequestParam int year) {
        List<IncomeDTO> incomes = incomeService.getIncomeForMonth(month, year);
        return ResponseEntity.ok(ApiResponse.success(incomes));
    }
}
