package com.three.recipingrecipeservicebe.recommendation.service;

import com.three.recipingrecipeservicebe.global.config.OpenAiProperties;
import com.three.recipingrecipeservicebe.recommendation.dto.ChatGptRequestDto;
import com.three.recipingrecipeservicebe.recommendation.dto.ChatGptResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiRecommendService {

    private final OpenAiProperties properties;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public List<Long> chat(ChatGptRequestDto chatGptRequestDto) {
        String prompt = String.format(
                "1부터 %d까지의 숫자 중에서 중복 없이 %d개의 숫자를 무작위로 골라줘. 다른 말 하지 말고 배열만 출력해. 예: [3, 12, 99, ...]",
                chatGptRequestDto.getTotalRecipeCount(),
                chatGptRequestDto.getPickCount()
        );

        Map<String, Object> request = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        return webClient.post()
                .uri("/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getApiKey())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatGptResponseDto.class)
                .map(response -> {
                    String content = response.getChoices().get(0).getMessage().getContent();
                    return parseNumberArray(content);
                })
                .block();
    }

    private List<Long> parseNumberArray(String content) {
        try {
            content = content.replaceAll("[\\[\\]]", "").trim(); // 대괄호 제거
            if (content.isEmpty()) return List.of();

            return Arrays.stream(content.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("응답 파싱 실패: {}", content, e);
            return List.of(); // 실패 시 빈 리스트 반환
        }
    }
}