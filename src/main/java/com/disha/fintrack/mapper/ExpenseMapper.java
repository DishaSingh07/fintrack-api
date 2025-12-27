package com.disha.fintrack.mapper;

import com.disha.fintrack.dto.ExpenseDTO;
import com.disha.fintrack.entity.ExpenseEntity;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper implements MapperInterface<ExpenseDTO, ExpenseEntity> {

    @Override
    public ExpenseDTO toDto(ExpenseEntity entity) {
        if (entity == null) {
            return null;
        }
        String categoryName = (entity.getCategory() != null) ? entity.getCategory().getName() : null;
        return ExpenseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .amount(entity.getAmount())
                .date(entity.getDate())
                .categoryName(categoryName)
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public ExpenseEntity toEntity(ExpenseDTO dto) {
        if (dto == null) {
            return null;
        }
        return ExpenseEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
