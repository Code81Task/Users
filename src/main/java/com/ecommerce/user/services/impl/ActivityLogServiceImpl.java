package com.ecommerce.user.services.impl;

import com.ecommerce.user.dto.ActivityLogDto;
import com.ecommerce.user.entity.ActivityLog;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.error.UserApiException;
import com.ecommerce.user.repos.ActivityLogRepo;
import com.ecommerce.user.repos.UserRepo;
import com.ecommerce.user.services.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepo activityLogRepo;
    private final UserRepo userRepo;

    @Override
    public ActivityLogDto createLog(ActivityLogDto dto) {
        User currentUser = getCurrentUser();
        ActivityLog saved = activityLogRepo.save(ActivityLogDto.toEntity(dto, currentUser));
        return ActivityLogDto.fromEntity(saved);
    }

    @Override
    public List<ActivityLogDto> getAllLogs() {
        User currentUser = getCurrentUser();
        return activityLogRepo.findAll()
                .stream()
                .filter(log -> log.getUser() != null && log.getUser().getId().equals(currentUser.getId()))
                .map(ActivityLogDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ActivityLogDto getLogById(Long id) {
        User currentUser = getCurrentUser();
        ActivityLog log = activityLogRepo.findById(id)
                .orElseThrow(() -> new UserApiException("ActivityLog not found with id: " + id));

        if (log.getUser() == null || !log.getUser().getId().equals(currentUser.getId())) {
            throw new UserApiException("You are not allowed to access this log");
        }

        return ActivityLogDto.fromEntity(log);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepo.findFirstByEmail(email)
                .orElseThrow(() -> new UserApiException("Authenticated user not found: " + email));
    }

    @Override
    public ActivityLogDto createLLog(ActivityLogDto dto, User user) {
        ActivityLog saved = activityLogRepo.save(ActivityLogDto.toEntity(dto, user));
        return ActivityLogDto.fromEntity(saved);
    }
}
