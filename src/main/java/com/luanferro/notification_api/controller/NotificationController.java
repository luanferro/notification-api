package com.luanferro.notification_api.controller;

import com.luanferro.notification_api.dto.NotificationRequest;
import com.luanferro.notification_api.entity.Notification;
import com.luanferro.notification_api.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody @Valid NotificationRequest request) {

        Notification notifcation = notificationService.save(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notifcation);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> findNotification(@PathVariable UUID id) {
        Notification notification = notificationService.findById(id);

        return ResponseEntity.ok(notification);
    }
}