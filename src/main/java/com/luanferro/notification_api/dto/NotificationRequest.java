package com.luanferro.notification_api.dto;

import com.luanferro.notification_api.entity.enums.NotificationChannel;
import com.luanferro.notification_api.entity.enums.NotificationPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record NotificationRequest(@NotNull NotificationChannel channel,
                                  @NotBlank String recipient,
                                  @NotNull NotificationPriority priority,
                                  String message,
                                  String templateName,
                                  Map<String, String> data
                                  ) {
}
