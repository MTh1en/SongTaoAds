package com.capstone.ads.mapper;

import com.capstone.ads.dto.notification.NotificationDTO;
import com.capstone.ads.model.NotificationStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mappings({
            @Mapping(target = "notificationId", source = "notification.id"),
            @Mapping(target = "type", source = "notification.type"),
            @Mapping(target = "roleTarget", source = "notification.roleTarget"),
            @Mapping(target = "userTarget", source = "notification.userTarget"),
            @Mapping(target = "message", source = "notification.message"),
            @Mapping(target = "createdAt", source = "notification.createdAt")
    })
    NotificationDTO toDTO(NotificationStatus notificationStatus);
}
