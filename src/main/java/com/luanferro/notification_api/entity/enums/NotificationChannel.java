package com.luanferro.notification_api.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum NotificationChannel {
    EMAIL,
    SMS,
    PUSH;

    @JsonCreator
    public static NotificationChannel fromString(String value){
        return NotificationChannel.valueOf(value.toUpperCase());
    }
}
