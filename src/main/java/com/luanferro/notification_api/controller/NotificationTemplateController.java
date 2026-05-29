package com.luanferro.notification_api.controller;

import com.luanferro.notification_api.dto.NotificationTemplateRequest;
import com.luanferro.notification_api.entity.NotificationTemplate;
import com.luanferro.notification_api.service.NotificationTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
public class NotificationTemplateController {

    private final NotificationTemplateService service;

    @PostMapping
    public ResponseEntity<NotificationTemplate> createNotificationTemplate(@Valid @RequestBody NotificationTemplateRequest request) {
        NotificationTemplate notificationTemplate = service.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(notificationTemplate);
    }

    @GetMapping("/{name}")
    public ResponseEntity<NotificationTemplate> findByName(@PathVariable String name) {
        NotificationTemplate notificationTemplate = service.findByName(name);

        return ResponseEntity.ok(notificationTemplate);
    }

}
