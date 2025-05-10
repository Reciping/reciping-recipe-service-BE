package com.three.recipingrecipeservicebe.bookmark.repository;

import com.three.recipingrecipeservicebe.bookmark.entity.RecipeBookmarkDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RecipeBookmarkRepository extends MongoRepository<RecipeBookmarkDocument, String> {

    // 1. 사용자가 즐겨찾기한 레시피 목록 조회
    Page<RecipeBookmarkDocument> findByUserId(Long userId, Pageable pageable);

    // 2. 특정 사용자의 특정 레시피 즐겨찾기 존재 여부
    Optional<RecipeBookmarkDocument> findByUserIdAndRecipeId(Long userId, Long recipeId);

    // 3. 특정 사용자의 즐겨찾기 개수 확인
    long countByUserId(Long userId);

}
