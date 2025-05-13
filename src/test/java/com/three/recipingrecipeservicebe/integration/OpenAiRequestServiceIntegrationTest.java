package com.three.recipingrecipeservicebe.integration;

import com.three.recipingrecipeservicebe.recommendation.dto.ChatGptRequestDto;
import com.three.recipingrecipeservicebe.recommendation.service.OpenAiRecommendService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OpenAiRequestServiceIntegrationTest {

    @Autowired
    private OpenAiRecommendService openAiRecommendService;

    private static final Long TOTAL_RECIPE_COUNT = 100L;
    private static final Long PICK_COUNT = 12L;

    @RepeatedTest(3)
    @DisplayName("OpenAI: 레시피 인덱스 12개를 배열로 정확히 반환해야 함")
    void testOpenAiRandomIndexArray() {
        // given
        ChatGptRequestDto requestDto = new ChatGptRequestDto(TOTAL_RECIPE_COUNT, PICK_COUNT);

        // when
        List<Long> numbers = openAiRecommendService.chat(requestDto);

        System.out.println(numbers);

        // then
        assertThat(numbers)
                .hasSize(PICK_COUNT.intValue())
                .doesNotHaveDuplicates()
                .allMatch(n -> n >= 1 && n <= TOTAL_RECIPE_COUNT);
    }
}