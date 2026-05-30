package com.luanferro.notification_api.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum NotificationPriority {
    HIGH,
    MEDIUM,
    LOW;

    @JsonCreator
    public static NotificationPriority fromString(String value){
        return NotificationPriority.valueOf(value.toUpperCase());
    }
}
