package com.ecommerce.user.dto;

import com.ecommerce.user.entity.Member;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String password;
    private LocalDate membershipDate;

    public static MemberDto fromEntity(Member member) {
        if (member == null) return null;
        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .password(member.getPassword())
                .email(member.getEmail())
                .phone(member.getPhone())
                .address(member.getAddress())
                .membershipDate(member.getMembershipDate())
                .build();
    }

    public static Member toEntity(MemberDto dto) {
        if (dto == null) return null;
        return Member.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password((dto.getPassword()))
                .address(dto.getAddress())
                .membershipDate(dto.getMembershipDate() != null ? dto.getMembershipDate() : LocalDate.now())
                .build();
    }
}
