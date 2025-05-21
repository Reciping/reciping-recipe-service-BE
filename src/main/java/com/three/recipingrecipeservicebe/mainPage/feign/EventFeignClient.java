package com.three.recipingrecipeservicebe.mainPage.feign;

import com.three.recipingrecipeservicebe.common.dto.Response;
import com.three.recipingrecipeservicebe.mainPage.dto.EventSummaryResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "reciping-event-service-BE", url = "http://172.16.24.34:8083")
public interface EventFeignClient {

    @GetMapping("/api/v1/events")
    Response<List<EventSummaryResponseDto>> getAllEventSummaries();

}