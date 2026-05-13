package com.luanferro.notification_api.dto;

import com.luanferro.notification_api.entity.enums.NotificationChannel;
import com.luanferro.notification_api.entity.enums.NotificationPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequest(@NotNull NotificationChannel channel,
                                  @NotBlank String recipient,
                                  @NotBlank String message,
                                  @NotNull NotificationPriority priority) {
}
