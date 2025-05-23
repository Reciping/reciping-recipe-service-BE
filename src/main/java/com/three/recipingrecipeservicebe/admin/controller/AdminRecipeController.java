package com.three.recipingrecipeservicebe.admin.controller;

import com.three.recipingrecipeservicebe.admin.dto.AdminRecipeDto;
import com.three.recipingrecipeservicebe.admin.dto.AdminRecipeRequestDto;
import com.three.recipingrecipeservicebe.admin.service.AdminRecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/recipes")
@RequiredArgsConstructor
public class AdminRecipeController {

    private final AdminRecipeService adminRecipeService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<AdminRecipeDto>> getRecipeList(Pageable pageable) {
        Page<AdminRecipeDto> response = adminRecipeService.getRecipeList(pageable);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRecipe(
            @PathVariable Long id,
            @RequestBody @Valid AdminRecipeRequestDto requestDto
    ) {
        adminRecipeService.updateRecipe(id, requestDto);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        adminRecipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
