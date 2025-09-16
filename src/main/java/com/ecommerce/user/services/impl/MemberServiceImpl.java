package com.ecommerce.user.services.impl;

import com.ecommerce.user.dto.MemberDto;
import com.ecommerce.user.dto.SignupRequest;
import com.ecommerce.user.entity.Member;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.enums.UserRole;
import com.ecommerce.user.error.UserApiException; // Import exception
import com.ecommerce.user.repos.MemberRepo;
import com.ecommerce.user.repos.UserRepo;
import com.ecommerce.user.services.AuthService;
import com.ecommerce.user.services.MemberService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private final MemberRepo memberRepo;
    private final AuthService authService;

    @Override
    @Transactional
    public MemberDto createMember(MemberDto dto) {
        try {
            validateMemberDto(dto);

            if (userRepo.findFirstByEmail(dto.getEmail()).isPresent()) {
                throw new UserApiException("A member with this email already exists: " + dto.getEmail());
            }

            String rawPassword = dto.getPassword();
            String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
            dto.setPassword(encodedPassword);

            Member savedMember = memberRepo.save(MemberDto.toEntity(dto));

            SignupRequest signupRequest = new SignupRequest();
            signupRequest.setEmail(dto.getEmail());
            signupRequest.setPassword(rawPassword);
            signupRequest.setUserName(dto.getName());

            authService.createUser(signupRequest);

            return MemberDto.fromEntity(savedMember);

        } catch (UserApiException e) {
            throw e;
        } catch (Exception e) {
            throw new UserApiException("Failed to create member: " + e.getMessage());
        }
    }

    @Override
    public List<MemberDto> getAllMembers() {
        try {
            return memberRepo.findAll()
                    .stream()
                    .map(MemberDto::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new UserApiException("Failed to retrieve members list");
        }
    }

    @Override
    public MemberDto getMemberById(Long id) {
        if (id == null || id <= 0) {
            throw new UserApiException("Invalid member ID");
        }

        Member member = memberRepo.findById(id)
                .orElseThrow(() -> new UserApiException("Member not found with ID: " + id));
        return MemberDto.fromEntity(member);
    }

    @Override
    public MemberDto updateMember(Long id, MemberDto dto) {
        if (id == null || id <= 0) {
            throw new UserApiException("Invalid member ID");
        }

        validateMemberDto(dto);

        Member existing = memberRepo.findById(id)
                .orElseThrow(() -> new UserApiException("Member not found with ID: " + id));

        if (!existing.getEmail().equals(dto.getEmail()) &&
                userRepo.findFirstByEmail(dto.getEmail()).isPresent()) {
            throw new UserApiException("Email already used by another member: " + dto.getEmail());
        }

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setAddress(dto.getAddress());

        try {
            return MemberDto.fromEntity(memberRepo.save(existing));
        } catch (Exception e) {
            throw new UserApiException("Failed to update member data: " + e.getMessage());
        }
    }

    @PostConstruct
    public void createDefaultAccounts() {
        try {
            User normalUserAccount = userRepo.findByUserRole(UserRole.MEMBER);
            if (normalUserAccount == null) {

                MemberDto dto = MemberDto.builder()
                        .address("cairo")
                        .phone("01050566622")
                        .name("mohamed gamal")
                        .email("user@test.com")
                        .password("user")
                        .build();

                String rawPassword = dto.getPassword();
                String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
                dto.setPassword(encodedPassword);

                Member savedMember = memberRepo.save(MemberDto.toEntity(dto));

                SignupRequest signupRequest = new SignupRequest();
                signupRequest.setEmail(dto.getEmail());
                signupRequest.setPassword(rawPassword);
                signupRequest.setUserName(dto.getName());

                authService.createUser(signupRequest);
            }

        } catch (Exception e) {
            throw new UserApiException("Failed to create default accounts during initialization: " + e.getMessage());
        }
    }

    private void validateMemberDto(MemberDto dto) {
        if (dto == null) {
            throw new UserApiException("Member data is required");
        }

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new UserApiException("Member name is required");
        }

        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new UserApiException("Member email is required");
        }

        if (!isValidEmail(dto.getEmail())) {
            throw new UserApiException("Invalid member email");
        }

        if (dto.getPassword() == null || dto.getPassword().length() < 4) {
            throw new UserApiException("Password must be at least 4 characters");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
