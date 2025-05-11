package com.three.recipingrecipeservicebe.hashtag.service;

import com.three.recipingrecipeservicebe.hashtag.entity.RecipeTagDocument;
import com.three.recipingrecipeservicebe.hashtag.repository.RecipeTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeTagService {

    private final RecipeTagRepository recipeTagRepository;

    public List<String> findTagsByRecipeId(Long recipeId) {
        return recipeTagRepository.findByRecipeId(recipeId)
                .map(RecipeTagDocument::getTags)
                .orElse(List.of());
    }

    public void saveTags(Long recipeId, List<String> tags) {
        recipeTagRepository.save(
                RecipeTagDocument.builder()
                        .recipeId(recipeId)
                        .tags(tags)
                        .build()
        );
    }
}