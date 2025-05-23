package com.three.recipingrecipeservicebe.admin.entity;

import com.three.recipingrecipeservicebe.admin.dto.AdminRecipeRequestDto;
import com.three.recipingrecipeservicebe.recipe.entity.DishType;
import com.three.recipingrecipeservicebe.recipe.entity.IngredientType;
import com.three.recipingrecipeservicebe.recipe.entity.MethodType;
import com.three.recipingrecipeservicebe.recipe.entity.SituationType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "recipes")
public class AdminRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "cooking_time")
    private String cookingTime;

    @Column(length = 10)
    private String difficulty;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private DishType dishType;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private SituationType situationType;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private MethodType methodType;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private IngredientType ingredientType;

    @Column(name = "object_name", length = 100)
    private String objectName;

    @Column(name = "key_name", length = 100)
    private String keyName;

    @Column(name = "file_path", length = 100)
    private String filePath;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public void updateFromAdminDto(AdminRecipeRequestDto dto) {
        if (dto.getTitle() != null) this.title = dto.getTitle();
        if (dto.getContent() != null) this.content = dto.getContent();
    }

    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

}
