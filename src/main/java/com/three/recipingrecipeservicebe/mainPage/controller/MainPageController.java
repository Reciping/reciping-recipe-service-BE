package com.three.recipingrecipeservicebe.mainPage.controller;

import com.three.recipingrecipeservicebe.mainPage.dto.MainPageResponseDto;
import com.three.recipingrecipeservicebe.mainPage.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/main")
public class MainPageController {

    private final MainPageService mainPageService;

    @GetMapping
    public ResponseEntity<MainPageResponseDto> getMainPageContents(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam String position,
            Pageable pageable
    ) {
        MainPageResponseDto response = mainPageService.getMainPageContents(userId, position, pageable);
        return ResponseEntity.ok(response);
    }
}
