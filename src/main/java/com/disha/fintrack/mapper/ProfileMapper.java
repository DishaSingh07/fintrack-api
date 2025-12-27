package com.disha.fintrack.mapper;

import com.disha.fintrack.dto.ProfileDTO;
import com.disha.fintrack.entity.ProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper implements MapperInterface<ProfileDTO, ProfileEntity> {

    @Override
    public ProfileDTO toDto(ProfileEntity entity) {
        if(entity == null) {
            return null;
        }
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setEmail(entity.getEmail());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    @Override
    public ProfileEntity toEntity(ProfileDTO dto) {
        if(dto == null) {
            return null;
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setId(dto.getId());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
