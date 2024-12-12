package com.miniblog.image.controller;

import com.miniblog.image.dto.RequestDTO;
import com.miniblog.image.dto.ResponseDTO;
import com.miniblog.image.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/image-service/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/presigned-url")
    public ResponseEntity<ResponseDTO> generatePresignedUrl(
            @Valid @RequestBody RequestDTO requestDTO) {
        ResponseDTO responseDTO = imageService.generatePresignedUrl(requestDTO);

        return ResponseEntity.ok(responseDTO);
    }


//    @PostMapping("/presigned-url")
//    public ResponseEntity<Map<String, String>> generatePresignedUrl(
//            @RequestBody Map<String, String> request) {
//        String originalFileName = request.get("fileName");
//        String fileType = request.get("fileType");
//        String objectKey = UUID.randomUUID().toString() + "_" + originalFileName;
//        log.info("generatePresignedUrl : originalFileName={}, fileType={} objectKey={}",originalFileName, fileType, objectKey);
//        // PutObjectRequest 생성
//        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(objectKey)
//                .contentType(fileType)
//                .build();
//
//        // Presigned URL 생성
//        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(15))
//                .putObjectRequest(putObjectRequest)
//                .build();
//
//        String presignedUrl = s3Presigner.presignPutObject(putObjectPresignRequest).url().toString();
//        log.info("generatePresignedUrl : presignedUrl={}",presignedUrl);
//        Map<String, String> response = new HashMap<>();
//        response.put("presignedUrl", presignedUrl);
//        response.put("objectKey", objectKey);
//
//        return ResponseEntity.ok(response);
//    }
}
