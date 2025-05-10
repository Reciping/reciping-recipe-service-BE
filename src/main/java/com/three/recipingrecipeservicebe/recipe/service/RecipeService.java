package com.three.recipingrecipeservicebe.recipe.service;

import com.three.recipingrecipeservicebe.bookmark.service.RecipeBookmarkService;
import com.three.recipingrecipeservicebe.global.exception.custom.FileUploadException;
import com.three.recipingrecipeservicebe.global.exception.custom.ForbiddenException;
import com.three.recipingrecipeservicebe.hashtag.entity.HashTag;
import com.three.recipingrecipeservicebe.hashtag.entity.RecipeTagDocument;
import com.three.recipingrecipeservicebe.hashtag.repository.HashTagRepository;
import com.three.recipingrecipeservicebe.hashtag.repository.RecipeTagRepository;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeCreatedResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeListResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeRequestDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeDetailResponseDto;
import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import com.three.recipingrecipeservicebe.recipe.entity.RecipeMapper;
import com.three.recipingrecipeservicebe.recipe.infrastructure.s3.S3Uploader;
import com.three.recipingrecipeservicebe.recipe.mapper.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j(topic = "RecipeService")
@RequiredArgsConstructor
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final S3Uploader s3Uploader;
    // TODO [강산하] [2025-05-09]: 리포지토리는 하나만 의존성 주입, 나머지는 역할 분리를 위해 서비스로 변환 필요
    private final RecipeTagRepository recipeTagRepository;
    private final HashTagRepository hashTagRepository;
    private final RecipeBookmarkService recipeBookmarkService;

    @Transactional(readOnly = true)
    public RecipeDetailResponseDto getRecipeById(Long id, Long userId) {
        // 레시피 상세 정보
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        // TAG 가져오기
        List<String> tags = recipeTagRepository.findByRecipeId(id)
                .map(RecipeTagDocument::getTags)
                .orElse(List.of()); // 없으면 빈 리스트

        // 즐겨찾기 가져오기
        boolean isBookmarked = recipeBookmarkService.isBookmarked(userId, id);

        RecipeDetailResponseDto baseDto = recipeMapper.toDto(recipe);

        // 레시피 상세 정보와 태그 결합
        return baseDto.toBuilder().isBookmarked(isBookmarked).tags(tags).build();
    }

    @Transactional(readOnly = true)
    public List<RecipeListResponseDto> getRecipeListByPage(Pageable pageable) {
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
        dto = processImageUpdate(recipe, dto, file);

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

    private void saveTags(RecipeRequestDto dto, Recipe recipe) {
        List<String> tags = dto.getTags();
        if (tags != null && !tags.isEmpty()) {
            recipeTagRepository.save(
                    RecipeTagDocument.builder().recipeId(recipe.getId()).tags(tags).build()
            );

            for (String tag : tags) {
                hashTagRepository.findByName(tag)
                        .orElseGet(() -> hashTagRepository.save(
                                HashTag.builder().name(tag).build()));
            }
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

    private RecipeRequestDto processImageUpdate(Recipe recipe, RecipeRequestDto dto, MultipartFile file) {
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