package com.ecommerce.user.controller;

import com.ecommerce.user.dto.MemberDto;
import com.ecommerce.user.error.UserApiException;
import com.ecommerce.user.services.MemberService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/signup")
    @RateLimiter(name = "loginRateLimiter")
    public ResponseEntity<MemberDto> createMember(@Valid @RequestBody MemberDto memberDto) {
        try {
            MemberDto createdMember = memberService.createMember(memberDto);
            return new ResponseEntity<>(createdMember, HttpStatus.CREATED);
        } catch (UserApiException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/auth")
    @RateLimiter(name = "loginRateLimiter")
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        List<MemberDto> members = memberService.getAllMembers();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/auth/{id}")
    @RateLimiter(name = "loginRateLimiter")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable Long id) {
        try {
            MemberDto member = memberService.getMemberById(id);
            return new ResponseEntity<>(member, HttpStatus.OK);
        } catch (UserApiException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/auth/{id}")
    @RateLimiter(name = "loginRateLimiter")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long id, @Valid @RequestBody MemberDto memberDto) {
        try {
            MemberDto updatedMember = memberService.updateMember(id, memberDto);
            return new ResponseEntity<>(updatedMember, HttpStatus.OK);
        } catch (UserApiException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}