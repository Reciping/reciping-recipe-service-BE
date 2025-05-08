package com.three.recipingrecipeservicebe.recipe.entity;

import com.three.recipingrecipeservicebe.recipe.dto.RecipeRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "recipes")
@SQLDelete(sql = "UPDATE recipes SET deleted_at = now(), is_deleted = true WHERE id = ?;")
@SQLRestriction(value = "is_deleted = false")
public class Recipe extends BaseEntity {

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
    private Integer cookingTime;

    @Column(length = 10)
    private String difficulty;

    @Column(length = 50)
    private String category;

    @Column(length = 50)
    private String occasion;

    @Column(name = "method", length = 50)
    private String method;

    @Column(length = 50)
    private String ingredient;

    @Column(name = "object_name", length = 100)
    private String objectName;

    @Column(name = "key_name", length = 100)
    private String keyName;

    @Column(name = "file_path", length = 100)
    private String filePath;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public static Recipe createFromDto(RecipeRequestDto dto, Long userId) {
        return Recipe.builder()
                .userId(userId)
                .title(dto.getTitle())
                .content(dto.getContent())
                .cookingTime(dto.getCookingTime())
                .difficulty(dto.getDifficulty())
                .category(dto.getCategory())
                .occasion(dto.getOccasion())
                .method(dto.getMethod())
                .ingredient(dto.getIngredient())
                .keyName(dto.getKeyName())
                .imageUrl(dto.getImageUrl())
                .filePath(dto.getFilePath())
                .objectName(dto.getObjectName())
                .build();
    }

    public void updateFromDto(RecipeRequestDto dto) {
        if (dto.getTitle() != null) this.title = dto.getTitle();
        if (dto.getContent() != null) this.content = dto.getContent();
        if (dto.getCookingTime() != null) this.cookingTime = dto.getCookingTime();
        if (dto.getDifficulty() != null) this.difficulty = dto.getDifficulty();
        if (dto.getCategory() != null) this.category = dto.getCategory();
        if (dto.getOccasion() != null) this.occasion = dto.getOccasion();
        if (dto.getMethod() != null) this.method = dto.getMethod();
        if (dto.getIngredient() != null) this.ingredient = dto.getIngredient();

        if (dto.getObjectName() != null) this.objectName = dto.getObjectName();
        if (dto.getKeyName() != null) this.keyName = dto.getKeyName();
        if (dto.getFilePath() != null) this.filePath = dto.getFilePath();

        // null 이면 null 삽입
        this.imageUrl = dto.getImageUrl();
    }

}
