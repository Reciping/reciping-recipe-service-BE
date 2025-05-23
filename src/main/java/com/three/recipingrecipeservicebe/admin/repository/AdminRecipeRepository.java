package com.three.recipingrecipeservicebe.admin.repository;

import com.three.recipingrecipeservicebe.admin.entity.AdminRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRecipeRepository extends JpaRepository<AdminRecipe, Long> {
}
