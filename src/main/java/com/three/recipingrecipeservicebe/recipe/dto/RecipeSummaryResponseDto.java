package com.three.recipingrecipeservicebe.recipe.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RecipeSummaryResponseDto {

    private Long id;
    private String title;
    private String imageUrl;
    private int likeCount;
    private boolean isLiked;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}