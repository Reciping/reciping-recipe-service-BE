package com.three.recipingrecipeservicebe.hashtag.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "recipe_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeTagDocument {

    @Id
    private Long recipeId;

    private List<String> tags;

}
