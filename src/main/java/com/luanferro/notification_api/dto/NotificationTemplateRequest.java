package com.luanferro.notification_api.dto;

import com.luanferro.notification_api.entity.enums.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationTemplateRequest(
        @NotBlank String name,
        @NotBlank String content,
        @NotNull NotificationChannel channel
) {}
