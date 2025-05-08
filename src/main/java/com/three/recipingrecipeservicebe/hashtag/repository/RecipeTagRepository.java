package com.three.recipingrecipeservicebe.hashtag.repository;

import com.three.recipingrecipeservicebe.hashtag.entity.RecipeTagDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeTagRepository extends MongoRepository<RecipeTagDocument, Long> {

    // 레시피 ID로 해당 태그 목록 조회
    Optional<RecipeTagDocument> findByRecipeId(Long recipeId);

    // 특정 태그 중 하나라도 포함된 레시피 리스트 조회 (추천용)
    List<RecipeTagDocument> findByTagsIn(List<String> tags);

    // 레시피 삭제 시 태그 연결 제거용
    void deleteByRecipeId(Long recipeId);
}
