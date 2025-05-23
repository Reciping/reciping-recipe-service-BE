package com.three.recipingrecipeservicebe.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminRecipeDto {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private boolean isDeleted;

}