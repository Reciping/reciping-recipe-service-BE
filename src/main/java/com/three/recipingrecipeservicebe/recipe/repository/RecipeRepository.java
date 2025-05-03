package com.three.recipingrecipeservicebe.recipe.repository;

import com.three.recipinguserservicebe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<User, Long> {

}
