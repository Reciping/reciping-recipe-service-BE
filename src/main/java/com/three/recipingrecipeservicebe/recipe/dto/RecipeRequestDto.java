package com.three.recipingrecipeservicebe.recipe.dto;

import com.three.recipingrecipeservicebe.recipe.entity.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RecipeRequestDto {

    private Long userId;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @Size(max = 500, message = "내용은 500자 이하로 작성해주세요.")
    private String content;

    private CookingTime cookingTime;
    private Difficulty difficulty;

    private DishType dishType;
    private SituationType situationType;
    private MethodType methodType;
    private IngredientType ingredientType;

    @Size(max = 100)
    private String objectName;

    @Size(max = 100)
    private String keyName;

    @Size(max = 100)
    private String filePath;

    @Size(max = 500)
    private String imageUrl;

    @Size(max = 5)
    private List<String> tags;

    private Boolean shouldRemoveImage;

}
