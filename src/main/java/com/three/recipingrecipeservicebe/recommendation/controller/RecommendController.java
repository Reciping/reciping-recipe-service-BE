package com.three.recipingrecipeservicebe.recommendation.controller;

import com.three.recipingrecipeservicebe.recommendation.dto.RecommendRequestDto;
import com.three.recipingrecipeservicebe.recommendation.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping
    public ResponseEntity<List<Long>> getRecommendations(@RequestBody RecommendRequestDto requestDto) {
        List<Long> result = recommendService.getRecommendations(requestDto);
        return ResponseEntity.ok(result);
    }
}
