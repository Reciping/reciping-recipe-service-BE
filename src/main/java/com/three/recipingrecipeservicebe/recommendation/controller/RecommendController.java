package com.three.recipingrecipeservicebe.recommendation.controller;

import com.three.recipingrecipeservicebe.global.logger.CustomLogger;
import com.three.recipingrecipeservicebe.global.logger.LogType;
import com.three.recipingrecipeservicebe.global.util.JsonStringifier;
import com.three.recipingrecipeservicebe.recommendation.dto.RecommendRequestDto;
import com.three.recipingrecipeservicebe.recommendation.service.RecommendService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     private static final Logger logger = LoggerFactory.getLogger(RecommendController.class); // If CustomLogger.track is used
    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");

    @PostMapping
    public ResponseEntity<List<Long>> getRecommendations(@RequestBody RecommendRequestDto requestDto, HttpServletRequest httpRequest) {
        try {
            List<Long> result = recommendService.getRecommendations(requestDto);

            CustomLogger.track(
                    logger,
                    LogType.VIEW, // Assuming 'VIEW' is appropriate, or use a more specific LogType if available
                    "user_recommendations", // Generic targetId, can be more specific if requestDto has user info
                    JsonStringifier.toJsonString(requestDto),
                    httpRequest
            );

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            errorLogger.error("Error in RecommendController.getRecommendations(): {}", e.getMessage(), e);
            throw e;
        }
    }
}