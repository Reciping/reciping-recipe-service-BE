package com.three.recipingrecipeservicebe.hashtag.service;

import com.three.recipingrecipeservicebe.hashtag.entity.HashTag;
import com.three.recipingrecipeservicebe.hashtag.entity.TagCount;
import com.three.recipingrecipeservicebe.hashtag.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HashTagAggregateService {

    private final MongoTemplate mongoTemplate;
    private final HashTagRepository hashTagRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void updateTagCounts() {
        List<TagCount> aggregated = aggregateTagCounts();

        for (TagCount tagCount : aggregated) {
            hashTagRepository.findByName(tagCount.getName())
                    .ifPresentOrElse(existing -> {
                        existing.setCount(tagCount.getCount());
                        hashTagRepository.save(existing);
                    }, () -> {
                        hashTagRepository.save(HashTag.builder()
                                .name(tagCount.getName())
                                .count(tagCount.getCount())
                                .build());
                    });
        }
    }

    private List<TagCount> aggregateTagCounts() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("tags"),
                Aggregation.group("tags").count().as("count")
        );

        AggregationResults<TagCount> results = mongoTemplate.aggregate(
                aggregation, "recipe_tags", TagCount.class
        );

        return results.getMappedResults();
    }
}
