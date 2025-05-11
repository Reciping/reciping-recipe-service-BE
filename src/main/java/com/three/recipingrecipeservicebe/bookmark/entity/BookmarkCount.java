package com.three.recipingrecipeservicebe.bookmark.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkCount {

    @Field("_id")
    private Long recipeId;

    private long count;
}
