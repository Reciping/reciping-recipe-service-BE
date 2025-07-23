package com.three.recipingrecipeservicebe.repsitory;

import com.three.recipingrecipeservicebe.hashtag.entity.HashTag;
import com.three.recipingrecipeservicebe.hashtag.repository.HashTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
public class HashTagRepositoryTest {

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:6");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
    }

    @Autowired
    private HashTagRepository hashTagRepository;

    @BeforeEach
    void setUp() {
        hashTagRepository.deleteAll();
    }

    @Test
    @DisplayName("CREATE 해시태그 중복 없이 생성")
    void createHashTag() {
        // given
        String name = "짜장면";
        HashTag tag = HashTag.builder()
                .name(name)
                .count(1L)
                .build();

        // when
        HashTag saved = hashTagRepository.findByName(name)
                .orElseGet(() -> hashTagRepository.save(tag));

        // then
        assertThat(saved.getName()).isEqualTo("짜장면");
    }

    @Test
    @DisplayName("CREATE 여러 해시태그 중복 없이 저장 (DuplicateKeyException 방지)")
    void createMultipleHashTags_withoutDuplicates() {
        // given
        List<String> tagNames = List.of("짜장면", "짬뽕", "탕수육");

        // DB에 이미 존재하는 이름들 조회
        List<HashTag> existingTags = hashTagRepository.findAllByNameIn(tagNames);
        Set<String> existingNames = existingTags.stream()
                .map(HashTag::getName)
                .collect(Collectors.toSet());

        // 중복 제외하고 저장할 새 태그만 추출
        List<HashTag> newTags = tagNames.stream()
                .filter(name -> !existingNames.contains(name))
                .map(name -> HashTag.builder()
                        .name(name)
                        .count(1L)
                        .build())
                .toList();

        // when
        hashTagRepository.saveAll(newTags);

        // then
        List<HashTag> allTags = hashTagRepository.findAll();
        assertThat(allTags).extracting(HashTag::getName)
                .contains("짜장면", "짬뽕", "탕수육");
    }
}
