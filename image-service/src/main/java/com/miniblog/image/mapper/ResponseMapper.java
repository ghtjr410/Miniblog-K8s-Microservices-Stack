package com.miniblog.image.mapper;

import com.miniblog.image.dto.ResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {
    public ResponseDTO toResponseDTO(String presignedUrl, String objectKey) {
        return new ResponseDTO(
                presignedUrl,
                objectKey);
    }
}