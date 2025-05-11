package com.three.recipingrecipeservicebe.bookmark.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkRequestDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long recipeId;

}
