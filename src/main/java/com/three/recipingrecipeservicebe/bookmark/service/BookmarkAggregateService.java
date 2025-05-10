package com.three.recipingrecipeservicebe.bookmark.service;

import com.three.recipingrecipeservicebe.bookmark.entity.BookmarkCount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkAggregateService {

    private final MongoTemplate mongoTemplate;

    @Scheduled(cron = "0 0 * * * *") // 매 시간 정각 실행
    public void aggregateRecipeBookmarkCount() {
        List<BookmarkCount> results = aggregate();

        // 기존 데이터 삭제 후 재삽입 (덮어쓰기)
        mongoTemplate.dropCollection("bookmark_stats");
        mongoTemplate.insert(results, "bookmark_stats");
    }

    private List<BookmarkCount> aggregate() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("recipeId").count().as("count")
        );

        AggregationResults<BookmarkCount> results = mongoTemplate.aggregate(
                aggregation,
                "recipe_bookmarks",      // 원본 컬렉션
                BookmarkCount.class
        );

        return results.getMappedResults();
    }
}
