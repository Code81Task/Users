package com.ecommerce.user.services;

import com.ecommerce.user.dto.MemberDto;

import java.util.List;

public interface MemberService {
    MemberDto createMember(MemberDto dto);
    List<MemberDto> getAllMembers();
    MemberDto getMemberById(Long id);
    MemberDto updateMember(Long id, MemberDto dto);
}
