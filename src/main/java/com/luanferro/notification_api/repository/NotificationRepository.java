package com.luanferro.notification_api.repository;

import com.luanferro.notification_api.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
