package com.three.recipingrecipeservicebe.hashtag.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hashtag_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HashTag {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private Long count;

}
