package com.disha.fintrack.mapper;

import com.disha.fintrack.dto.IncomeDTO;
import com.disha.fintrack.entity.IncomeEntity;
import org.springframework.stereotype.Component;

@Component
public class IncomeMapper implements MapperInterface<IncomeDTO, IncomeEntity> {

    @Override
    public IncomeDTO toDto(IncomeEntity entity) {
        if (entity == null) {
            return null;
        }
        String categoryName = (entity.getCategory() != null) ? entity.getCategory().getName() : null;
        return IncomeDTO.builder()
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
    public IncomeEntity toEntity(IncomeDTO dto) {
        if (dto == null) {
            return null;
        }
        return IncomeEntity.builder()
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
