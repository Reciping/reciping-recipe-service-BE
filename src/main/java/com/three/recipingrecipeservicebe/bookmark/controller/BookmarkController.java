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
    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<BookmarkResponseDto>> getBookmarksByUser(
            @PathVariable Long userId,
            Pageable pageable,
            HttpServletRequest request
    ) {
        try {
            Page<BookmarkResponseDto> recipeBookmarks = recipeBookmarkService.getBookmarksByUserId(userId, pageable);

            CustomLogger.track(
                    logger,
                    LogType.VIEW,
                    String.valueOf(userId), // targetId: The user whose bookmarks are being viewed
                    "Pageable: " + pageable.toString(), // payload: Pagination info
                    request
            );

            return ResponseEntity.ok(recipeBookmarks);
        } catch (Exception e) {
            errorLogger.error("Error in BookmarkController.getBookmarksByUser() for userId {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/toggle")
    public ResponseEntity<Boolean> toggleBookmark(
            @RequestBody BookmarkRequestDto bookmarkRequestDto,
            HttpServletRequest request
    ) {
        try {
            boolean isBookmarked = recipeBookmarkService.toggleBookmark(bookmarkRequestDto);

            CustomLogger.track(
                    logger,
                    LogType.VIEW, // Or a more specific LogType.BOOKMARK_ACTION if available
                    String.valueOf(bookmarkRequestDto.getUserId()),
                    "{ \"recipeId\": \"" + bookmarkRequestDto.getRecipeId() + "\", \"isBookmarked\": " + isBookmarked + " }",
                    request
            );

            return ResponseEntity.ok(isBookmarked);
        } catch (Exception e) {
            errorLogger.error("Error in BookmarkController.toggleBookmark() for userId {} and recipeId {}: {}",
                    bookmarkRequestDto.getUserId(), bookmarkRequestDto.getRecipeId(), e.getMessage(), e);
            throw e;
        }
    }
}