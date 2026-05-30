package com.luanferro.notification_api.service;

import com.luanferro.notification_api.dto.NotificationTemplateRequest;
import com.luanferro.notification_api.entity.NotificationTemplate;
import com.luanferro.notification_api.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationTemplateService {

    private final NotificationTemplateRepository notificationTemplateRepository;

    public NotificationTemplate save(NotificationTemplateRequest request) {

        if (notificationTemplateRepository.findByName(request.name()).isPresent()) {
            throw new IllegalArgumentException("Template com esse nome já existe.");
        }

        NotificationTemplate notificationTemplate = new NotificationTemplate();

        notificationTemplate.setChannel(request.channel());
        notificationTemplate.setName(request.name());
        notificationTemplate.setContent(request.content());

        return notificationTemplateRepository.save(notificationTemplate);
    }

    public NotificationTemplate findByName(String name) {
        return notificationTemplateRepository.findByName(name).orElseThrow(() -> new RuntimeException("Notification template not found."));
    }
}
