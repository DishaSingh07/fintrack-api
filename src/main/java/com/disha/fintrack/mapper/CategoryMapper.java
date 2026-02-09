package com.disha.fintrack.mapper;

import com.disha.fintrack.dto.CategoryDTO;
import com.disha.fintrack.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper implements MapperInterface<CategoryDTO, CategoryEntity> {

    @Override
    public CategoryDTO toDto(CategoryEntity entity) {
        if(entity == null) {
            return null;
        }
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setProfileId(entity.getProfile() != null ? entity.getProfile().getId() : null);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setIcon(entity.getIcon());
        return dto;
    }

    @Override
    public CategoryEntity toEntity(CategoryDTO dto) {
        if(dto == null) {
            return null;
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setIcon(dto.getIcon());
        return entity;
    }
}
