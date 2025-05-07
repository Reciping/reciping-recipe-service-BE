package com.three.recipingrecipeservicebe.recipe.infrastructure.s3;

import com.three.recipingrecipeservicebe.global.exception.custom.FileUploadException;
import com.three.recipingrecipeservicebe.global.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader implements FileUploadService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new FileUploadException("파일명이 비어있습니다.");
        }

        String ext = getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString();
        String keyName =  "recipes/" + uuid + ext;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(keyName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
        return getUrl(keyName);
    }

    @Override
    public void delete(String imageUrl) {

        String keyName = extractKeyFromUrl(imageUrl);

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(keyName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    private String getUrl(String keyName) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucket, keyName);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private String extractKeyFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("imageUrl이 비어 있습니다.");
        }

        int index = imageUrl.indexOf(".amazonaws.com/");
        if (index == -1) {
            throw new IllegalArgumentException("S3 imageUrl 형식이 아닙니다: " + imageUrl);
        }

        return imageUrl.substring(index + ".amazonaws.com/".length() + 1);
    }
}