package com.three.recipingrecipeservicebe.recipe.service;

import com.three.recipingrecipeservicebe.bookmark.dto.BookmarkResponseDto;
import com.three.recipingrecipeservicebe.bookmark.service.RecipeBookmarkService;
import com.three.recipingrecipeservicebe.global.exception.custom.FileUploadException;
import com.three.recipingrecipeservicebe.global.exception.custom.ForbiddenException;
import com.three.recipingrecipeservicebe.hashtag.service.HashTagService;
import com.three.recipingrecipeservicebe.hashtag.service.RecipeTagService;
import com.three.recipingrecipeservicebe.recipe.dto.*;
import com.three.recipingrecipeservicebe.recipe.entity.*;
import com.three.recipingrecipeservicebe.recipe.infrastructure.s3.S3Uploader;
import com.three.recipingrecipeservicebe.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j(topic = "RecipeService")
@RequiredArgsConstructor
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final RecipeTagService recipeTagService;
    private final HashTagService hashTagService;
    private final RecipeBookmarkService recipeBookmarkService;

    private final RecipeMapper recipeMapper;
    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    public RecipeDetailResponseDto getRecipeById(Long id, Long userId) {
        // 레시피 상세 정보
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        // TAG 가져오기
        List<String> tags = recipeTagService.findTagsByRecipeId(id);

        // 즐겨찾기 가져오기
        boolean isBookmarked = recipeBookmarkService.isBookmarked(userId, id);

        RecipeDetailResponseDto baseDto = recipeMapper.toDto(recipe);

        // 레시피 상세 정보와 태그 결합
        return baseDto.toBuilder().isBookmarked(isBookmarked).tags(tags).build();
    }

    public RecipeCountResponseDto getMyRecipeCount(Long userId) {
        Long count = recipeRepository.countByUserIdAndIsDeletedFalse(userId);
        return new RecipeCountResponseDto(userId, count);
    }

    @Transactional(readOnly = true)
    public List<RecipeSummaryResponseDto> getRecipeListByPage(Pageable pageable) {
        return recipeRepository.findPagedByCreatedAtDesc(pageable).stream()
                .map(recipeMapper::toListDto)
                .toList();
    }

    @Transactional
    public RecipeCreatedResponseDto createRecipe(RecipeRequestDto dto, Long userId, MultipartFile file) throws FileUploadException {
        // S3 이미지 처리
        String imageUrl = processImageIfExists(file);

        dto = dto.toBuilder()
                .imageUrl(imageUrl)
                .build();

        // DB 저장
        Recipe recipe = Recipe.createFromDto(dto, userId);
        Recipe savedRecipe = recipeRepository.save(recipe);

        // TAGS 몽고디비 저장
        saveTags(dto, savedRecipe);

        return recipeMapper.toCreatedDto(savedRecipe);
    }

    @Transactional
    public void updateRecipe(Long id, RecipeRequestDto dto, Long userId, MultipartFile file) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        if (!recipe.getUserId().equals(userId)) {
            throw new ForbiddenException("수정 권한이 없습니다.");
        }

        // S3 이미지 수정
        dto = processImageUpdate(dto, file);

        // TAGS 몽고디비 저장
        saveTags(dto, recipe);

        recipe.updateFromDto(dto);
    }

    @Transactional
    public void deleteRecipe(Long id, Long userId) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        if (!recipe.getUserId().equals(userId)) {
            throw new ForbiddenException("수정 권한이 없습니다.");
        }

        recipeRepository.delete(recipe);
    }

    public Page<RecipeSummaryResponseDto> getMyRecipes(Long userId, Pageable pageable) {
        Page<Recipe> recipePage = recipeRepository.findByUserIdAndIsDeletedFalse(userId, pageable);
        return recipePage.map(recipeMapper::toMyRecipeSummaryDto);
    }

    public Page<RecipeSummaryResponseDto> getBookmarkedRecipeList(Long userId, Pageable pageable) {
        // 1. 북마크 도큐먼트 조회
        Page<BookmarkResponseDto> bookmarkPage = recipeBookmarkService.getBookmarksByUserId(userId, pageable);

        // 2. 북마크된 recipeId 추출 (순서 유지됨)
        List<Long> recipeIds = bookmarkPage.getContent().stream()
                .map(BookmarkResponseDto::getRecipeId)
                .toList();

        // 3. RDB 에서 레시피 조회 (순서 보장 안됨)
        List<Recipe> recipes = recipeRepository.findByIdInAndIsDeletedFalse(recipeIds);

        // 4. Map 으로 매핑 (id → Recipe)
        Map<Long, Recipe> recipeMap = recipes.stream()
                .collect(Collectors.toMap(Recipe::getId, Function.identity()));

        // 5. 북마크 순서대로 정렬
        List<Recipe> orderedRecipes = recipeIds.stream()
                .map(recipeMap::get)            // 순서대로 매핑
                .filter(Objects::nonNull)       // 혹시 누락된 ID 방지
                .toList();

        // 6. DTO 매핑
        List<RecipeSummaryResponseDto> mapped = orderedRecipes.stream()
                .map(recipeMapper::toMyRecipeSummaryDto)
                .toList();

        // 7. 페이징 복원
        return new PageImpl<>(mapped, pageable, bookmarkPage.getTotalElements());
    }

    public Map<String, List<Map<String, String>>> getAllCategoryOptions() {
        Map<String, List<Map<String, String>>> response = new LinkedHashMap<>();

        response.put("dish", toOptionList(DishType.values()));
        response.put("situation", toOptionList(SituationType.values()));
        response.put("ingredient", toOptionList(IngredientType.values()));
        response.put("method", toOptionList(MethodType.values()));

        return response;
    }

    public Page<RecipeSummaryResponseDto> searchRecipes(RecipeSearchConditionRequestDto condition, Pageable pageable) {
        Page<Recipe> page = recipeRepository.searchByCondition(condition, pageable);
        return page.map(recipeMapper::toListDto); // DTO로 변환하면서 Page 유지
    }

    private <E extends Enum<E> & EnumWithLabel> List<Map<String, String>> toOptionList(E[] enums) {
        return Arrays.stream(enums)
                .map(e -> Map.of(
                        "value", e.name(),
                        "label", e.getLabel()
                ))
                .toList();
    }

    private void saveTags(RecipeRequestDto dto, Recipe recipe) {
        List<String> tags = dto.getTags();
        if (tags != null && !tags.isEmpty()) {
            recipeTagService.saveTags(recipe.getId(), tags);
            hashTagService.saveIfNotExists(tags);
        }
    }

    private String processImageIfExists(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        try {
            return s3Uploader.upload(file);
        } catch (IOException e) {
            throw new FileUploadException("파일 업로드 실패");
        }
    }

    private RecipeRequestDto processImageUpdate(RecipeRequestDto dto, MultipartFile file) {
        String imageUrl = dto.getImageUrl();

        // 이미지 삭제 요청
        if (Boolean.TRUE.equals(dto.getShouldRemoveImage())) {
            if (imageUrl != null) {
                s3Uploader.delete(imageUrl); // imageUrl 직접 삭제
            }
            imageUrl = null;
        }

        // 이미지 업로드 요청
        if (file != null && !file.isEmpty()) {
            if (imageUrl != null) {
                s3Uploader.delete(imageUrl); // 기존 이미지 삭제
            }
            imageUrl = processImageIfExists(file); // 새 이미지 업로드 후 URL 반환
        }

        return dto.toBuilder()
                .imageUrl(imageUrl)
                .build();
    }

    // TODO [강산하] [2025-05-05]: 엘라스틱서치 일치율 검색 결과 ID 리스트로 전달받아, 해당 ID 기준으로 쿼리 호출하도록 구현

}