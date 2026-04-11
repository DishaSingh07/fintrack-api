package com.disha.fintrack.mapper;

import com.disha.fintrack.dto.TransactionDTO;
import com.disha.fintrack.entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper implements MapperInterface<TransactionDTO, TransactionEntity> {
    @Override
    public TransactionDTO toDto(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }

        return TransactionDTO.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .category(entity.getCategory().getName())
                .date(entity.getDate())
                .name(entity.getName())
                .type(entity.getType().name())
                .userId(entity.getProfile().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

    }

    @Override
    public TransactionEntity toEntity(TransactionDTO dto) {
       if(dto == null) {
           return null;
       }

        return TransactionEntity.builder()
                .id(dto.getId())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .name(dto.getName())
                .icon(dto.getIcon())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
