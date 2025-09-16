package com.ecommerce.user.dto;

import com.ecommerce.user.entity.ActivityLog;
import com.ecommerce.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogDto {
    private Long id;
    private Long userId;
    private String action;
    private LocalDateTime timestamp;
    private String details;

    public static ActivityLogDto fromEntity(ActivityLog log) {
        if (log == null) return null;
        return ActivityLogDto.builder()
                .id(log.getId())
                .userId(log.getUser() != null ? log.getUser().getId() : null)
                .action(log.getAction())
                .timestamp(log.getTimestamp())
                .details(log.getDetails())
                .build();
    }

    public static ActivityLog toEntity(ActivityLogDto dto, User user) {
        if (dto == null) return null;
        return ActivityLog.builder()
                .id(dto.getId())
                .user(user)
                .action(dto.getAction())
                .timestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now())
                .details(dto.getDetails())
                .build();
    }
}
