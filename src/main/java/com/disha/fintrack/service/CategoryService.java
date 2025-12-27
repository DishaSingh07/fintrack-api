package com.disha.fintrack.service;

import com.disha.fintrack.dto.CategoryDTO;
import java.util.List;

public interface CategoryService {


    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO getCategoryById(Long id);

    List<CategoryDTO> getCategoriesByProfileId(Long profileId);

    List<CategoryDTO> getCategoriesForCurrentProfile();

    List<CategoryDTO> getCategoriesByType(String type, Long profileId);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);

    List<CategoryDTO> getAllUserCategories();

    boolean categoryExists(String name, Long profileId);

    List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type);
}
