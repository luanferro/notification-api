package com.luanferro.notification_api.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum NotificationStatus {
    PENDING,
    SENT,
    FAILED;

    @JsonCreator
    public static NotificationStatus fromString(String value){
        return NotificationStatus.valueOf(value.toUpperCase());
    }
}
