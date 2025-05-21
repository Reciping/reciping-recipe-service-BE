package com.three.recipingrecipeservicebe.mainPage.dto;

import com.three.recipingrecipeservicebe.mainPage.entity.AdPosition;
import com.three.recipingrecipeservicebe.mainPage.entity.AdStatus;
import com.three.recipingrecipeservicebe.mainPage.entity.AdType;
import com.three.recipingrecipeservicebe.mainPage.entity.BillingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdResponse {
    private Long id;
    private Long advertiserId;
    private String title;
    private AdType adType;
    private String imageUrl;
    private String targetUrl;
    private AdPosition preferredPosition;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private BillingType billingType;
    private Long budget;
    private Long spentAmount;
    private AdStatus status;
    private Float score;
}
