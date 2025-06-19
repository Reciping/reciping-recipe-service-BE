package com.three.recipingrecipeservicebe.mainPage.controller;

import com.three.recipingrecipeservicebe.global.logger.CustomLogger;
import com.three.recipingrecipeservicebe.global.logger.LogType;
import com.three.recipingrecipeservicebe.mainPage.dto.MainPageResponseDto;
import com.three.recipingrecipeservicebe.mainPage.service.MainPageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/main")
public class MainPageController {

    private final MainPageService mainPageService;
    private static final Logger logger = LoggerFactory.getLogger(MainPageController.class);
    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");

    @GetMapping
    public ResponseEntity<MainPageResponseDto> getMainPageContents(
            @RequestParam String position,
            Pageable pageable,
            HttpServletRequest request
    ) {
        try {
            MainPageResponseDto response = mainPageService.getMainPageContents(position, pageable);

            CustomLogger.track(
                    logger,
                    LogType.VIEW,
                    "main_page", // targetId: Identifier for the main page content
                    "{ \"position\": \"" + position + "\", \"pageable\": \"" + pageable.toString() + "\" }", // payload: Position and pagination
                    request
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            errorLogger.error("Error in MainPageController.getMainPageContents() for position {}: {}", position, e.getMessage(), e);
            throw e;
        }
    }
}