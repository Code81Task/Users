package com.ecommerce.user.repos;

import com.ecommerce.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepo  extends JpaRepository<Member,Long> {
}
