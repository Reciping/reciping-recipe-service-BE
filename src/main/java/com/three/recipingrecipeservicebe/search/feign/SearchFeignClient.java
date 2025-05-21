package com.three.recipingrecipeservicebe.search.feign;

import com.three.recipingrecipeservicebe.search.dto.RecipeSearchCursor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "reciping-search-service-BE", url = "http://172.16.24.34:8084")
public interface SearchFeignClient {

    @GetMapping("/api/v1/search/menu")
    RecipeSearchCursor searchByMenu(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "0") int pageCount
    );

    @GetMapping("/api/v1/search/ingredients")
    RecipeSearchCursor searchByIngredients(
            @RequestParam List<String> ingredients,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "0") int pageCount
    );

    @GetMapping("/api/v1/search/natural")
    RecipeSearchCursor searchByNaturalQuery(
            @RequestParam String query,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "0") int pageCount
    );

    @GetMapping("/api/v1/search/tags")
    RecipeSearchCursor searchByTagsWithPagination(
            @RequestParam List<String> tags,
            @RequestParam(defaultValue = "false") boolean matchAll,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "0") int pageCount
    );

}