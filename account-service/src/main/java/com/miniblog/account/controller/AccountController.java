package com.miniblog.account.controller;

import com.miniblog.account.service.account.AccountService;
import com.miniblog.account.validation.ValidUuid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account-service")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AccountController {

    private final AccountService accountService;

    @DeleteMapping ("/user")
    public ResponseEntity<?> deleteAccount(
            @RequestHeader(value = "X-User-Sub", required = false) @ValidUuid String userUuid
    ) {
        accountService.deleteUser(userUuid);
        return ResponseEntity.noContent().build();
    }
}
