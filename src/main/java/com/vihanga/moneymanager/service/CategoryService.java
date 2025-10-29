package com.vihanga.moneymanager.service;

import com.vihanga.moneymanager.dto.CategoryDto;
import com.vihanga.moneymanager.entity.CategoryEntity;
import com.vihanga.moneymanager.entity.ProfileEntity;
import com.vihanga.moneymanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

    //save category
    public CategoryDto saveCategory(CategoryDto categoryDto){

        ProfileEntity profile = profileService.getCurrentProfile();

        if (categoryRepository.existsByNameAndProfileId(categoryDto.getName(),profile.getId())){
            throw new RuntimeException("Category with this name is already exist");
        }

        CategoryEntity newCategory = toEntity(categoryDto,profile);
        newCategory = categoryRepository.save(newCategory);

        return toDto(newCategory);
    }

    //get categories for current user
    public List<CategoryDto> getCategoriesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(profile.getId());
        return categories.stream().map(this::toDto).toList();
    }

    //getCategories By Type For Current User
    public List<CategoryDto> getCategoriesByTypeForCurrentUser(String type){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> entities = categoryRepository.findByTypeAndProfileId(type, profile.getId());
        return entities.stream().map(this::toDto).toList();
    }

    //update category
    public CategoryDto updateCategory(Long categoryId,CategoryDto categoryDto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId, profile.getId())
                .orElseThrow(()->new RuntimeException("Category is not found or not accessible"));
        existingCategory.setName(categoryDto.getName());
        existingCategory.setIcon(categoryDto.getIcon());
        existingCategory.setType(categoryDto.getType());
        existingCategory = categoryRepository.save(existingCategory);
        return toDto(existingCategory);
    }

    //Helper methods
    private CategoryEntity toEntity(CategoryDto categoryDto, ProfileEntity profile){
        return CategoryEntity.builder()
                .name(categoryDto.getName())
                .icon(categoryDto.getIcon())
                .profile(profile)
                .type(categoryDto.getType())
                .build();
    }
    private CategoryDto toDto(CategoryEntity categoryEntity){
        return CategoryDto.builder()
                .id(categoryEntity.getId())
                .profileId(categoryEntity.getProfile() != null ? categoryEntity.getProfile().getId():null)
                .name(categoryEntity.getName())
                .icon(categoryEntity.getIcon())
                .createdAt(categoryEntity.getCreateAt())
                .updatedAt(categoryEntity.getUpdateAt())
                .type(categoryEntity.getType())
                .build();
    }

}
