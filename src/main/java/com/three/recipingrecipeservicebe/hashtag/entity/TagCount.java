package com.three.recipingrecipeservicebe.hashtag.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagCount {

    @Field("_id")
    private String name;

    private Long count;
}
