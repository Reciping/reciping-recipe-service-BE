package com.three.recipingrecipeservicebe.mainPage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventSummaryResponseDto {
    private String id;
    private String title;
    private ImageInfo previewImage;
}
