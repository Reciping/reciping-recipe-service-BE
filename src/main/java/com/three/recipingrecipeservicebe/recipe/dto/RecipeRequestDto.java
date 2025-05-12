package com.three.recipingrecipeservicebe.recipe.dto;

import com.three.recipingrecipeservicebe.recipe.entity.DishType;
import com.three.recipingrecipeservicebe.recipe.entity.IngredientType;
import com.three.recipingrecipeservicebe.recipe.entity.MethodType;
import com.three.recipingrecipeservicebe.recipe.entity.SituationType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RecipeRequestDto {

    @NotNull(message = "유저 ID는 필수입니다.")
    private Long userId;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @Size(max = 500, message = "내용은 500자 이하로 작성해주세요.")
    private String content;

    @Min(value = 1, message = "요리 시간은 1분 이상이어야 합니다.")
    @Max(value = 1440, message = "요리 시간은 24시간(1440분) 이하여야 합니다.")
    private Integer cookingTime;

    @Size(max = 10, message = "코드값은 10자 이하여야 합니다.")
    private String difficulty;

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
