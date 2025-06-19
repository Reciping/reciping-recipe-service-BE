package com.three.recipingrecipeservicebe.recommendation.controller;

import com.three.recipingrecipeservicebe.global.logger.CustomLogger;
import com.three.recipingrecipeservicebe.global.logger.LogType;
import com.three.recipingrecipeservicebe.global.util.JsonStringifier; // Assuming JsonStringifier is accessible
import com.three.recipingrecipeservicebe.recommendation.dto.ChatGptRequestDto;
import com.three.recipingrecipeservicebe.recommendation.service.OpenAiRecommendService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class OpenAiController {

    private final OpenAiRecommendService openAiRecommendService;
    private static final Logger logger = LoggerFactory.getLogger(OpenAiController.class);
    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");

    @PostMapping
    public ResponseEntity<List<Long>> chat(
            @RequestBody ChatGptRequestDto chatGptRequestDto,
            HttpServletRequest request
    ) {
        try {
            List<Long> result = openAiRecommendService.chat(chatGptRequestDto);

            CustomLogger.track(
                    logger,
                    LogType.INFO, // Or a more specific type like LogType.CHAT_REQUEST
                    "chat_gpt_interaction", // targetId
                    JsonStringifier.toJsonString(chatGptRequestDto), // payload: the request DTO
                    request
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            errorLogger.error("Error in OpenAiController.chat(): {}", e.getMessage(), e);
            throw e;
        }
    }
}