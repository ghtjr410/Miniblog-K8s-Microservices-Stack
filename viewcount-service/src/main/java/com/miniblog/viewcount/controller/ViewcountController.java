package com.miniblog.viewcount.controller;

import com.miniblog.viewcount.service.viewcount.ViewcountService;
import com.miniblog.viewcount.validation.ValidUuid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/viewcount-service/posts")
@RequiredArgsConstructor
@Validated
public class ViewcountController {
    private final ViewcountService viewcountService;

    @PostMapping("/{postUuid}")
    public ResponseEntity<Void> incrementViewcount(
            @PathVariable @ValidUuid String postUuid) {
        viewcountService.incrementViewcount(postUuid);
        return ResponseEntity.noContent().build();
    }
}
