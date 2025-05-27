package com.three.recipingrecipeservicebe.bookmark.service;

import com.three.recipingrecipeservicebe.bookmark.dto.BookmarkRequestDto;
import com.three.recipingrecipeservicebe.bookmark.mapper.BookmarkMapper;
import com.three.recipingrecipeservicebe.bookmark.dto.BookmarkResponseDto;
import com.three.recipingrecipeservicebe.bookmark.entity.RecipeBookmarkDocument;
import com.three.recipingrecipeservicebe.bookmark.repository.RecipeBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeBookmarkService {

    private final RecipeBookmarkRepository recipeBookmarkRepository;
    private final BookmarkMapper bookmarkMapper;

    public Page<BookmarkResponseDto> getBookmarksByUserId(Long userId, Pageable pageable) {
        Page<RecipeBookmarkDocument> page = recipeBookmarkRepository.findByUserId(userId, pageable);
        return page.map(bookmarkMapper::toResponseDto);
    }

    public boolean toggleBookmark(BookmarkRequestDto bookmarkRequestDto) {
        Optional<RecipeBookmarkDocument> bookmark = recipeBookmarkRepository.findByUserIdAndRecipeId(bookmarkRequestDto.getUserId(), bookmarkRequestDto.getRecipeId());

        if (bookmark.isPresent()) {
            recipeBookmarkRepository.delete(bookmark.get());
            return false;
        }

        long currentCount = recipeBookmarkRepository.countByUserId(bookmarkRequestDto.getUserId());
        if (currentCount >= 30) {
            throw new IllegalStateException("즐겨찾기는 최대 30개까지만 가능합니다.");
        }

        RecipeBookmarkDocument newBookmark = RecipeBookmarkDocument.builder()
                .userId(bookmarkRequestDto.getUserId())
                .recipeId(bookmarkRequestDto.getRecipeId())
                .build();

        recipeBookmarkRepository.save(newBookmark);
        return true;
    }

    public boolean isBookmarked(Long userId, Long recipeId) {
        if (userId == null || recipeId == null) {
            return false;
        }
        return recipeBookmarkRepository.findByUserIdAndRecipeId(userId, recipeId).isPresent();
    }
}
