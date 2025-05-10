package com.three.recipingrecipeservicebe.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class BookmarkResponseDto {

    private String id;
    private Long userId;
    private Long recipeId;
    private LocalDateTime createdAt;

}
