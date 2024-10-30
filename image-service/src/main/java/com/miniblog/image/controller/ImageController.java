package com.miniblog.image.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {
    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final S3Presigner s3Presigner;

    @PostMapping("/presigned-url")
    public ResponseEntity<Map<String, String>> generatePresignedUrl(
            @RequestBody Map<String, String> request) {
        String originalFileName = request.get("fileName");
        String fileType = request.get("fileType");
        String objectKey = UUID.randomUUID().toString() + "_" + originalFileName;

        // PutObjectRequest 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(fileType)
                .build();

        // Presigned URL 생성
        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(putObjectRequest)
                .build();

        String presignedUrl = s3Presigner.presignPutObject(putObjectPresignRequest).url().toString();

        Map<String, String> response = new HashMap<>();
        response.put("presignedUrl", presignedUrl);
        response.put("objectKey", objectKey);

        return ResponseEntity.ok(response);
    }
}
