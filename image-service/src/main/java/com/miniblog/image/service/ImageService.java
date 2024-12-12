package com.miniblog.image.service;

import com.miniblog.image.dto.RequestDTO;
import com.miniblog.image.dto.ResponseDTO;
import com.miniblog.image.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final S3Presigner s3Presigner;
    private final ResponseMapper responseMapper;

    public ResponseDTO generatePresignedUrl(RequestDTO requestDTO) {
        String objectKey = UUID.randomUUID().toString() + "_" + requestDTO.fileName();
        log.info("generatePresignedUrl : originalFileName={}, fileType={} objectKey={}",requestDTO.fileName(), requestDTO.fileType(), objectKey);
        // PutObjectRequest 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(requestDTO.fileType())
                .build();

        // Presigned URL 생성
        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(putObjectRequest)
                .build();

        String presignedUrl = s3Presigner.presignPutObject(putObjectPresignRequest).url().toString();
        log.info("generatePresignedUrl : presignedUrl={}",presignedUrl);
        return responseMapper.toResponseDTO(presignedUrl,objectKey);
    }
}
