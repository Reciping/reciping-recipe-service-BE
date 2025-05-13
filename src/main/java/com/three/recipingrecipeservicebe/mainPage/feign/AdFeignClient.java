package com.three.recipingrecipeservicebe.mainPage.feign;

import com.three.recipingrecipeservicebe.mainPage.dto.AdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "reciping-ads-service-BE", url = "http://localhost:8083")
public interface AdFeignClient {

    @GetMapping("/api/v1/ads/public/serve")
    List<AdResponse> serveAdsByPosition(
            @RequestParam("position") String postion
    );

}
