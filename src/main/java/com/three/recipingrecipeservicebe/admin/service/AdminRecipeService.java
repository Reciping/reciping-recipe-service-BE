package com.three.recipingrecipeservicebe.admin.service;

import com.three.recipingrecipeservicebe.admin.dto.AdminRecipeDto;
import com.three.recipingrecipeservicebe.admin.dto.AdminRecipeRequestDto;
import com.three.recipingrecipeservicebe.admin.entity.AdminRecipe;
import com.three.recipingrecipeservicebe.admin.mapper.AdminRecipeMapper;
import com.three.recipingrecipeservicebe.admin.repository.AdminRecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminRecipeService {

    private final AdminRecipeRepository adminRecipeRepository;
    private final AdminRecipeMapper adminRecipeMapper;

    @Transactional
    public Page<AdminRecipeDto> getRecipeList(Pageable pageable) {
        return adminRecipeRepository.findAll(pageable)
                .map(adminRecipeMapper::toAdminDto);
    }

    @Transactional
    public void updateRecipe(Long id, AdminRecipeRequestDto dto) {
        AdminRecipe recipe = adminRecipeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("레시피 없음"));

        recipe.updateFromAdminDto(dto);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        AdminRecipe recipe = adminRecipeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("레시피 없음"));

        if (Boolean.TRUE.equals(recipe.getIsDeleted())) {
            throw new IllegalStateException("이미 삭제된 레시피입니다.");
        }

        recipe.softDelete();
    }
}
