package com.disha.fintrack.controller;

import com.disha.fintrack.dto.IncomeDTO;
import com.disha.fintrack.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDTO> addIncome(@RequestBody IncomeDTO incomeDTO) {
        IncomeDTO saved = incomeService.addIncome(incomeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeDTO> getIncomeById(@PathVariable Long id) {
        IncomeDTO income = incomeService.getIncomeById(id);
        return ResponseEntity.ok(income);
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<IncomeDTO>> getIncomesByProfileId(@PathVariable Long profileId) {
        List<IncomeDTO> incomes = incomeService.getIncomesByProfileId(profileId);
        return ResponseEntity.ok(incomes);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<IncomeDTO>> getIncomesByCategory(@PathVariable Long categoryId) {
        List<IncomeDTO> incomes = incomeService.getIncomesByCategory(categoryId);
        return ResponseEntity.ok(incomes);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getAllUserIncomes() {
        List<IncomeDTO> incomes = incomeService.getAllUserIncomes();
        return ResponseEntity.ok(incomes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeDTO> updateIncome(@PathVariable Long id, @RequestBody IncomeDTO incomeDTO) {
        IncomeDTO updated = incomeService.updateIncome(id, incomeDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/current-month")
    public ResponseEntity<List<IncomeDTO>> getCurrentMonthIncomes() {
        List<IncomeDTO> incomes = incomeService.getCurrentMonthIncomesForCurrentUser();
        return ResponseEntity.ok(incomes);
    }
}

