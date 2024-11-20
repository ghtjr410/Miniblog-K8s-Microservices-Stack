package com.miniblog.viewcount.controller;

import com.miniblog.viewcount.service.ViewcountService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/viewcount")
@RequiredArgsConstructor
@Validated
public class ViewcountController {
    private final ViewcountService viewcountService;

    @PostMapping("/{postUuid}")
    public ResponseEntity<Void> incrementViewcount(
            @PathVariable
            @NotBlank
            @Size(min = 36, max = 36) // UUID는 8-4-4-4-12 (총 36자)
            @Pattern(regexp = "^[0-9a-f\\-]{36}$") // 소문자 a-f, 숫자, 하이픈만 허용
            String postUuid) {
        viewcountService.incrementViewcount(postUuid);
        return ResponseEntity.noContent().build();
    }
}
