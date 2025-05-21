package com.three.recipingrecipeservicebe.mainPage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventSummaryResponseDto {
    private String id;
    private String title;
    private ImageInfo previewImage;
}
