package com.three.recipingrecipeservicebe.search.controller;

import com.three.recipingrecipeservicebe.global.logger.CustomLogger;
import com.three.recipingrecipeservicebe.global.logger.LogType;
import com.three.recipingrecipeservicebe.global.util.JsonStringifier; // Assuming JsonStringifier is accessible
import com.three.recipingrecipeservicebe.search.dto.RecipeSearchCursorResponse;
import com.three.recipingrecipeservicebe.search.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");

    @GetMapping("/menu")
    public RecipeSearchCursorResponse searchByMenu(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "0") int pageCount,
            HttpServletRequest request
    ) {
        try {
            RecipeSearchCursorResponse response = searchService.searchByMenu(keyword, size, cursor, pageCount);
            CustomLogger.track(
                    logger,
                    LogType.SEARCH,
                    "menu_search",
                    String.format("{ \"keyword\": \"%s\", \"size\": %d, \"cursor\": \"%s\", \"pageCount\": %d }", keyword, size, cursor == null ? "null" : cursor, pageCount),
                    request
            );
            return response;
        } catch (Exception e) {
            errorLogger.error("Error in SearchController.searchByMenu() with keyword '{}': {}", keyword, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/ingredients")
    public RecipeSearchCursorResponse searchByIngredients(
            @RequestParam List<String> ingredients,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "0") int pageCount,
            HttpServletRequest request
    ) {
        try {
            RecipeSearchCursorResponse response = searchService.searchByIngredients(ingredients, size, cursor, pageCount);
            CustomLogger.track(
                    logger,
                    LogType.SEARCH,
                    "ingredients_search",
                    String.format("{ \"ingredients\": %s, \"size\": %d, \"cursor\": \"%s\", \"pageCount\": %d }", JsonStringifier.toJsonString(ingredients), size, cursor == null ? "null" : cursor, pageCount),
                    request
            );
            return response;
        } catch (Exception e) {
            errorLogger.error("Error in SearchController.searchByIngredients() with ingredients '{}': {}", ingredients, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/natural")
    public RecipeSearchCursorResponse searchByNaturalQuery(
            @RequestParam String query,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "0") int pageCount,
            HttpServletRequest request
    ) {
        try {
            RecipeSearchCursorResponse response = searchService.searchByNatural(query, size, cursor, pageCount);
            CustomLogger.track(
                    logger,
                    LogType.SEARCH,
                    "natural_query_search",
                    String.format("{ \"query\": \"%s\", \"size\": %d, \"cursor\": \"%s\", \"pageCount\": %d }", query, size, cursor == null ? "null" : cursor, pageCount),
                    request
            );
            return response;
        } catch (Exception e) {
            errorLogger.error("Error in SearchController.searchByNaturalQuery() with query '{}': {}", query, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/tags")
    public RecipeSearchCursorResponse searchByTags(
            @RequestParam List<String> tags,
            @RequestParam(defaultValue = "false") boolean matchAll,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "0") int pageCount,
            HttpServletRequest request
    ) {
        try {
            RecipeSearchCursorResponse response = searchService.searchByTags(tags, matchAll, size, cursor, pageCount);
            CustomLogger.track(
                    logger,
                    LogType.SEARCH,
                    "tags_search",
                    String.format("{ \"tags\": %s, \"matchAll\": %b, \"size\": %d, \"cursor\": \"%s\", \"pageCount\": %d }", JsonStringifier.toJsonString(tags), matchAll, size, cursor == null ? "null" : cursor, pageCount),
                    request
            );
            return response;
        } catch (Exception e) {
            errorLogger.error("Error in SearchController.searchByTags() with tags '{}': {}", tags, e.getMessage(), e);
            throw e;
        }
    }
}