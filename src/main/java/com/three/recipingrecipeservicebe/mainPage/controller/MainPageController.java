package com.three.recipingrecipeservicebe.mainPage.controller;

import com.three.recipingrecipeservicebe.mainPage.dto.MainPageResponseDto;
import com.three.recipingrecipeservicebe.mainPage.service.MainPageService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public ResponseEntity<MainPageResponseDto> getMainPageContents(
            @RequestParam String position,
            Pageable pageable
    ) {
        MainPageResponseDto response = mainPageService.getMainPageContents(position, pageable);
        return ResponseEntity.ok(response);
    }
}
