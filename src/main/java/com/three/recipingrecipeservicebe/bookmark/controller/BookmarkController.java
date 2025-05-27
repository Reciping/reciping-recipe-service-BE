package com.three.recipingrecipeservicebe.bookmark.controller;

import com.three.recipingrecipeservicebe.bookmark.dto.BookmarkRequestDto;
import com.three.recipingrecipeservicebe.bookmark.dto.BookmarkResponseDto;
import com.three.recipingrecipeservicebe.bookmark.service.RecipeBookmarkService;
import com.three.recipingrecipeservicebe.global.logger.CustomLogger;
import com.three.recipingrecipeservicebe.global.logger.LogType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final RecipeBookmarkService recipeBookmarkService;
    private static final Logger logger = LoggerFactory.getLogger(BookmarkController.class);

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<BookmarkResponseDto>> getBookmarksByUser(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        Page<BookmarkResponseDto> recipeBookmarks = recipeBookmarkService.getBookmarksByUserId(userId, pageable);
        return ResponseEntity.ok(recipeBookmarks);
    }

    @PostMapping("/toggle")
    public ResponseEntity<Boolean> toggleBookmark(
            @RequestBody BookmarkRequestDto bookmarkRequestDto,
            HttpServletRequest request
    ) {
        boolean isBookmarked = recipeBookmarkService.toggleBookmark(bookmarkRequestDto);

        CustomLogger.track(
                logger,
                LogType.VIEW,
                "/api/v1/bookmarks/" + bookmarkRequestDto.getRecipeId(),
                "GET",
                String.valueOf(bookmarkRequestDto.getUserId()),
                null,
                null,
                String.valueOf(isBookmarked),
                request
        );

        return ResponseEntity.ok(isBookmarked);
    }
}
