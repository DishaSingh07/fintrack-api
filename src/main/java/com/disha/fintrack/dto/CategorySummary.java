package com.disha.fintrack.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CategorySummary {
    private String category;
    private double amount;
}
