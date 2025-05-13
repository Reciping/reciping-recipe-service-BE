package com.three.recipingrecipeservicebe.mainPage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageInfo {
    private String objectName;  // 예: preview.jpg
    private String keyName;     // S3 저장 키 (UUID 등)
    private String filePath;    // S3 폴더 경로
}
