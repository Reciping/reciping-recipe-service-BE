package com.three.recipingrecipeservicebe.recommendation.service;

import com.three.recipingrecipeservicebe.recommendation.dto.RecommendRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8000")
            .build();

    public List<Long> getRecommendations(RecommendRequestDto dto) {
        return webClient.post()
                .uri("/recommendations/by-userinfo")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<?> rawList = (List<?>) response.get("recommended_recipe_ids");
                    return rawList.stream()
                            .map(item -> ((Number) item).longValue())
                            .collect(Collectors.toList());
                })
                .block();
    }
}
