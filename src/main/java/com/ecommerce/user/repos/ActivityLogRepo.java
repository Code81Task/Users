package com.ecommerce.user.repos;

import com.ecommerce.user.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepo extends JpaRepository<ActivityLog, Long> {
    }