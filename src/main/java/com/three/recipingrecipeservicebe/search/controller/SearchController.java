package com.three.recipingrecipeservicebe.search.controller;

import com.three.recipingrecipeservicebe.search.dto.RecipeSearchCursor;
import com.three.recipingrecipeservicebe.search.dto.RecipeSearchCursorResponse;
import com.three.recipingrecipeservicebe.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/menu")
    public RecipeSearchCursorResponse searchByMenu(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "0") int pageCount
    ) {
        return searchService.searchByMenu(keyword, size, cursor, pageCount);
    }

    @GetMapping("/ingredients")
    public RecipeSearchCursorResponse searchByIngredients(
            @RequestParam List<String> ingredients,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "0") int pageCount
    ) {
        return searchService.searchByIngredients(ingredients, size, cursor, pageCount);
    }

    @GetMapping("/natural")
    public RecipeSearchCursorResponse searchByNaturalQuery(
            @RequestParam String query,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "0") int pageCount
    ) {
        return searchService.searchByNatural(query, size, cursor, pageCount);
    }

    @GetMapping("/tags")
    public RecipeSearchCursorResponse searchByTags(
            @RequestParam List<String> tags,
            @RequestParam(defaultValue = "false") boolean matchAll,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "0") int pageCount
    ) {
        return searchService.searchByTags(tags, matchAll, size, cursor, pageCount);
    }
}
