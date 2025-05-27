package com.three.recipingrecipeservicebe.recommendation.controller;

import com.three.recipingrecipeservicebe.recommendation.dto.ChatGptRequestDto;
import com.three.recipingrecipeservicebe.recommendation.service.OpenAiRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class OpenAiController {

    private final OpenAiRecommendService openAiRecommendService;

    @PostMapping
    public ResponseEntity<List<Long>> chat(@RequestBody ChatGptRequestDto chatGptRequestDto) {
        List<Long> result = openAiRecommendService.chat(chatGptRequestDto);
        return ResponseEntity.ok(result);
    }
}
