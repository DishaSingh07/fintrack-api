package com.disha.fintrack.controller;

import com.disha.fintrack.common.ApiResponse;
import com.disha.fintrack.dto.TransactionDTO;
import com.disha.fintrack.entity.ProfileEntity;
import com.disha.fintrack.service.ProfileService;
import com.disha.fintrack.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
@CrossOrigin("*")
public class TransactionController {

    private final TransactionService transactionService;
    private final ProfileService profileservice;

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDTO>> addTransaction(@RequestBody TransactionDTO transactionDTO) {
        TransactionDTO saved = transactionService.addTransaction(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(saved, "Transaction created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDTO>> getTransactionById(@PathVariable Long id) {
        TransactionDTO transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(ApiResponse.success(transaction));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getAllUserTransactions(
            @RequestParam(required = false) String type) {
        ProfileEntity profile = profileservice.getCurrentProfile();
        List<TransactionDTO> transactions = transactionService.getTransactionsByProfileId(profile.getId(), type);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByProfileId(@PathVariable Long profileId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByProfileId(profileId, null);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByCategory(@PathVariable Long categoryId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getRecentTransactions() {
        List<TransactionDTO> transactions = transactionService.getRecentTransactions();
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/current-month")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getCurrentMonthTransactions() {
        List<TransactionDTO> transactions = transactionService.getCurrentMonthTransactionsForCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> filterTransactions(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        Sort sort = Sort.by(direction, sortBy);
        List<TransactionDTO> transactions = transactionService.filterTransactions(startDate, endDate, keyword, sort,
                null);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/month")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionForMonth(
            @RequestParam int month,
            @RequestParam int year) {
        List<TransactionDTO> transactions = transactionService.getTransactionForMonth(month, year);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDTO>> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO updated = transactionService.updateTransaction(id, transactionDTO);
        return ResponseEntity.ok(ApiResponse.success(updated, "Transaction updated successfully"));
    }

    @GetMapping("/current-month/total")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalCurrentMonth() {
        BigDecimal total = transactionService.getTotalTransactionForMonth(LocalDate.now().getMonthValue(),
                LocalDate.now().getYear(), null);
        return ResponseEntity.ok(ApiResponse.success(total));
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalTransaction(@RequestParam(required = false) String type) {
        BigDecimal total = transactionService.getTotalTransactionByDates(null, LocalDate.now(), type);
        return ResponseEntity.ok(ApiResponse.success(total));

    }

    @GetMapping("/month/total")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalForMonth(
            @RequestParam int month,
            @RequestParam int year) {
        BigDecimal total = transactionService.getTotalTransactionForMonth(month, year, null);
        return ResponseEntity.ok(ApiResponse.success(total));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Transaction deleted successfully"));
    }
}
