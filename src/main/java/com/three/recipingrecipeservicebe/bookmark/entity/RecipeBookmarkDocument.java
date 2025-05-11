package com.three.recipingrecipeservicebe.bookmark.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "recipe_bookmarks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeBookmarkDocument {

    @Id
    private String id;

    @Indexed
    private Long userId;

    @Indexed
    private Long recipeId;

    @CreatedDate
    private LocalDateTime createdAt;

}
