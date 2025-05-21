package com.three.recipingrecipeservicebe.bookmark.controller;

import com.three.recipingrecipeservicebe.bookmark.dto.BookmarkRequestDto;
import com.three.recipingrecipeservicebe.bookmark.dto.BookmarkResponseDto;
import com.three.recipingrecipeservicebe.bookmark.service.RecipeBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final RecipeBookmarkService recipeBookmarkService;

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
            @RequestBody BookmarkRequestDto bookmarkRequestDto
    ) {
        boolean isBookmarked = recipeBookmarkService.toggleBookmark(bookmarkRequestDto);
        return ResponseEntity.ok(isBookmarked);
    }
}
