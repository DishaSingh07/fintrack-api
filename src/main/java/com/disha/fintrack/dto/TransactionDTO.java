package com.disha.fintrack.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {
    private Long id;
    private Long userId;
    private LocalDate date;
    private BigDecimal amount;
    private String category;
    private String title;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
