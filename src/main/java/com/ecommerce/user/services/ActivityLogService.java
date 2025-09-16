package com.ecommerce.user.services;

import com.ecommerce.user.dto.ActivityLogDto;
import com.ecommerce.user.entity.User;

import java.util.List;

public interface ActivityLogService {
    ActivityLogDto createLog(ActivityLogDto dto);
    List<ActivityLogDto> getAllLogs();
    ActivityLogDto getLogById(Long id);
     ActivityLogDto createLLog(ActivityLogDto dto, User user);
}
