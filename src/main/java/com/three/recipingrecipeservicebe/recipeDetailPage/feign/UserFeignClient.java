package com.three.recipingrecipeservicebe.recipeDetailPage.feign;

import com.three.recipingrecipeservicebe.recipeDetailPage.dto.UserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reciping-user-service-BE", url = "http://172.16.24.34:8080")
public interface UserFeignClient {

    @GetMapping("/api/v1/internal/users/{userId}/info")
    UserInfoDto getUserInfo(@PathVariable("userId") Long userId);

}
