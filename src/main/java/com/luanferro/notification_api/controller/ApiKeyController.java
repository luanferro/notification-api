package com.luanferro.notification_api.controller;

import com.luanferro.notification_api.dto.ApiKeyRequest;
import com.luanferro.notification_api.entity.ApiKey;
import com.luanferro.notification_api.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService service;

    @PostMapping
    public ResponseEntity<ApiKey> createApiKey(@RequestBody @Valid ApiKeyRequest request) {
        ApiKey apiKey = service.createApiKey(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiKey);
    }

}
