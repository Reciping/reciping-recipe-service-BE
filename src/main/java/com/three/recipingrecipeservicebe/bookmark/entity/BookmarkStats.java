package com.three.recipingrecipeservicebe.bookmark.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "bookmark_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkStats {

    @Id
    private Long recipeId; // 도큐먼트의 _id, 레시피 ID로 사용

    private long bookmarkCount; // 즐겨찾기 수
    private LocalDateTime updatedAt; // 마지막 집계 시간
}
