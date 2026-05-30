package com.luanferro.notification_api.service;

import com.luanferro.notification_api.dto.ApiKeyRequest;
import com.luanferro.notification_api.entity.ApiKey;
import com.luanferro.notification_api.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository repository;

    public ApiKey createApiKey(ApiKeyRequest request) {
        ApiKey apiKey = new ApiKey();
        apiKey.setClientName(request.clientName());
        apiKey.setActive(true);
        apiKey.setApiKey(UUID.randomUUID().toString());

        return repository.save(apiKey);
    }

    public boolean validate(String apiKey) {
        return repository.findByApiKey(apiKey)
                .map(ApiKey::isActive)
                .orElse(false);
    }
}
