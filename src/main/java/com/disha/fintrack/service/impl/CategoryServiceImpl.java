package com.disha.fintrack.service.impl;

import com.disha.fintrack.dto.CategoryDTO;
import com.disha.fintrack.entity.CategoryEntity;
import com.disha.fintrack.entity.ProfileEntity;
import com.disha.fintrack.mapper.MapperInterface;
import com.disha.fintrack.repository.CategoryRepository;
import com.disha.fintrack.service.CategoryService;
import com.disha.fintrack.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;
    private final MapperInterface<CategoryDTO, CategoryEntity> mapper;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        categoryDTO.setProfileId(profile.getId());
        // check if category already exists for the profile
        if (categoryExists(categoryDTO.getName(), categoryDTO.getProfileId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with name " + categoryDTO.getName() + " already exists for the profile.");
        }

        CategoryEntity newCategory = mapper.toEntity(categoryDTO);
        newCategory.setProfile(profile);

        CategoryEntity saved = categoryRepository.save(newCategory);
        return mapper.toDto(saved);

    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        return null;
    }

    @Override
    public List<CategoryDTO> getCategoriesByProfileId(Long profileId) {
        return List.of();
    }

    @Override
    public List<CategoryDTO> getCategoriesForCurrentProfile() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(profile.getId());
        return categories.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<CategoryDTO> getCategoriesByType(String type, Long profileId) {
        return List.of();
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + id));
        if (!existingCategory.getProfile().getId().equals(profile.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update this category.");
        }
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setType(categoryDTO.getType());
        existingCategory.setIcon(categoryDTO.getIcon());
        CategoryEntity updatedCategory = categoryRepository.save(existingCategory);
        return mapper.toDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {

    }

    @Override
    public List<CategoryDTO> getAllUserCategories() {
        return List.of();
    }

    @Override
    public boolean categoryExists(String name, Long profileId) {
        return categoryRepository.existsByNameAndProfileId(name, profileId);
    }

    @Override
    public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByTypeAndProfileId(type, profile.getId());
        return categories.stream()
                .map(mapper::toDto)
                .toList();
    }
}
