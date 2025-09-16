package com.ecommerce.user.controllers;

import com.ecommerce.user.dto.ActivityLogDto;
import com.ecommerce.user.services.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-logs")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @PostMapping
    public ResponseEntity<ActivityLogDto> createLog(@RequestBody ActivityLogDto dto) {
        ActivityLogDto savedLog = activityLogService.createLog(dto);
        return ResponseEntity.ok(savedLog);
    }

    @GetMapping
    public ResponseEntity<List<ActivityLogDto>> getAllLogs() {
        List<ActivityLogDto> logs = activityLogService.getAllLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityLogDto> getLogById(@PathVariable Long id) {
        ActivityLogDto log = activityLogService.getLogById(id);
        return ResponseEntity.ok(log);
    }
}
