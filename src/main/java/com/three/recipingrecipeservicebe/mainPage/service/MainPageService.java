package com.three.recipingrecipeservicebe.mainPage.service;

import com.three.recipingrecipeservicebe.mainPage.dto.AdResponse;
import com.three.recipingrecipeservicebe.mainPage.dto.EventSummaryResponseDto;
import com.three.recipingrecipeservicebe.mainPage.dto.MainPageResponseDto;
import com.three.recipingrecipeservicebe.mainPage.feign.AdFeignClient;
import com.three.recipingrecipeservicebe.mainPage.feign.EventFeignClient;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeListResponseDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.service.RecipeDetailFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final AdFeignClient adFeignClient;
    private final EventFeignClient eventFeignClient;
    private final RecipeDetailFacade recipeDetailFacade;

    public MainPageResponseDto getMainPageContents(Long userId, String position, Pageable pageable) {
        List<AdResponse> ads = adFeignClient.serveAdsByPosition(position);
        List<EventSummaryResponseDto> events = eventFeignClient.getAllEventSummaries();
        RecipeListResponseDto recipeList = recipeDetailFacade.getRecommendListWithLikesResponseDto(userId, pageable);

        return MainPageResponseDto.builder()
                .ads(ads)
                .events(events)
                .recommendedRecipes(recipeList.getRecipes())
                .build();
    }
}
