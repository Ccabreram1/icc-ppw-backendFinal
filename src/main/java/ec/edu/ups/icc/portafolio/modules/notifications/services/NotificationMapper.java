package ec.edu.ups.icc.portafolio.modules.notifications.services;

import ec.edu.ups.icc.portafolio.modules.notifications.dtos.NotificationRequestDto;
import ec.edu.ups.icc.portafolio.modules.notifications.dtos.NotificationResponseDto;
import ec.edu.ups.icc.portafolio.modules.notifications.models.NotificationEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponseDto toDto(NotificationEntity notification) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUser().getId());
        dto.setUserName(notification.getUser().getName());
        dto.setUserEmail(notification.getUser().getEmail());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setRead(notification.isRead());
        dto.setActionUrl(notification.getActionUrl());
        dto.setMetadata(notification.getMetadata());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setReadAt(notification.getReadAt());
        return dto;
    }

    public NotificationEntity toEntity(NotificationRequestDto dto) {
        NotificationEntity entity = new NotificationEntity();
        entity.setTitle(dto.getTitle());
        entity.setMessage(dto.getMessage());
        entity.setType(dto.getType());
        entity.setActionUrl(dto.getActionUrl());
        entity.setMetadata(dto.getMetadata());
        entity.setRead(false);
        return entity;
    }

    public void updateEntity(NotificationRequestDto dto, NotificationEntity entity) {
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getMessage() != null) {
            entity.setMessage(dto.getMessage());
        }
        if (dto.getType() != null) {
            entity.setType(dto.getType());
        }
        if (dto.getActionUrl() != null) {
            entity.setActionUrl(dto.getActionUrl());
        }
        if (dto.getMetadata() != null) {
            entity.setMetadata(dto.getMetadata());
        }
    }
}