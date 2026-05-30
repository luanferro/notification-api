package com.luanferro.notification_api.dto;

import jakarta.validation.constraints.NotBlank;

public record ApiKeyRequest(@NotBlank String clientName) {
}
