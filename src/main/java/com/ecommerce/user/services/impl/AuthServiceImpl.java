package com.ecommerce.user.services.impl;

import com.ecommerce.user.dto.SignupRequest;
import com.ecommerce.user.dto.UserDto;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.enums.UserRole;
import com.ecommerce.user.error.UserApiException;
import com.ecommerce.user.repos.UserRepo;
import com.ecommerce.user.services.AuthService;
import com.ecommerce.user.utils.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserDetailsService userDetailesSerivce;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDto createUser(SignupRequest signupRequest) {
        if (userRepo.findFirstByEmail(signupRequest.getEmail()).isPresent()) {
            throw new UserApiException("User with this email already exists.");
        }
        try {
            User user = User.builder()
                    .email(signupRequest.getEmail())
                    .name(signupRequest.getUserName())
                    .password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                    .userRole(UserRole.MEMBER)
                    .createdAt(LocalDateTime.now())
                    .build();

            User createdUser = userRepo.save(user);
            return UserDto.fromEntity(createdUser);
        } catch (Exception e) {
            throw new UserApiException("Failed to create user. Please try again later.");
        }
    }

    @Override
    public Boolean hasUserWithEmail(String email) {
        try {
            return userRepo.findFirstByEmail(email).isPresent();
        } catch (Exception e) {
            throw new UserApiException("Error checking if email exists: " + email);
        }
    }

    @Override
    public UserDto createAdminAccount(SignupRequest signupRequest) {
        if (userRepo.findFirstByEmail(signupRequest.getEmail()).isPresent()) {
            throw new UserApiException("Admin with this email already exists.");
        }

        User user = User.builder()
                .email(signupRequest.getEmail())
                .name(signupRequest.getUserName())
                .password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .userRole(UserRole.ADMIN)
                .createdAt(LocalDateTime.now())

                .build();

        User createdAdmin = userRepo.save(user);
        return UserDto.fromEntity(createdAdmin);
    }

    @Override
    public UserDto createStaffAccount(SignupRequest signupRequest) {
        if (userRepo.findFirstByEmail(signupRequest.getEmail()).isPresent()) {
            throw new UserApiException("Admin with this email already exists.");
        }

        User user = User.builder()
                .email(signupRequest.getEmail())
                .name(signupRequest.getUserName())
                .password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .userRole(UserRole.STAFF)
                .createdAt(LocalDateTime.now())

                .build();

        User createdAdmin = userRepo.save(user);
        return UserDto.fromEntity(createdAdmin);
    }

    @Override
    public UserDto createLibrarianAccount(SignupRequest signupRequest) {
        if (userRepo.findFirstByEmail(signupRequest.getEmail()).isPresent()) {
            throw new UserApiException("Librarian with this email already exists.");
        }

        User user = User.builder()
                .email(signupRequest.getEmail())
                .name(signupRequest.getUserName())
                .password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .userRole(UserRole.lIBRARIAN)
                .build();

        User createdAdmin = userRepo.save(user);
        return UserDto.fromEntity(createdAdmin);    }


    @Override
    public Optional<User> getUserByEmail(String email) {
        try {
            return userRepo.findFirstByEmail(email);
        } catch (Exception e) {
            throw new UserApiException("Error fetching user by email: " + email);
        }
    }

    @Override
    public Optional<User> getCurrentUser(String token) {
        try {
            String cleanToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
            String email = jwtUtil.extractUserName(cleanToken);  // يحقق التوقيع implicitly

            if (email == null || email.isEmpty()) {
                throw new UserApiException("Invalid token: email not found.");
            }

            UserDetails userDetails = userDetailesSerivce.loadUserByUsername(email);  // أضف هذا (افترض إن userDetailsService متاح via Autowire)
            if (!jwtUtil.validateToken(cleanToken, userDetails)) {  // أضف التحقق الكامل
                throw new UserApiException("Invalid or expired token.");
            }

            return userRepo.findFirstByEmail(email);
        } catch (Exception e) {
            throw new UserApiException("Failed to get current user from token: " + e.getMessage());
        }
    }



//    add default data
@PostConstruct
public void createDefaultAccounts() {
    try {
        User adminAccount = userRepo.findByUserRole(UserRole.ADMIN);
        if (adminAccount == null) {
            User admin = new User();
            admin.setEmail("admin@test.com");
            admin.setName("Mohamed Gamal");
            admin.setPassword(bCryptPasswordEncoder.encode("admin"));
            admin.setUserRole(UserRole.ADMIN);
            userRepo.save(admin);
        }

        User librarianAccount = userRepo.findByUserRole(UserRole.lIBRARIAN);
        if (librarianAccount == null) {
            User librarian = new User();
            librarian.setEmail("librarian@test.com");
            librarian.setName("Default Librarian");
            librarian.setPassword(bCryptPasswordEncoder.encode("librarian"));
            librarian.setUserRole(UserRole.lIBRARIAN);
            userRepo.save(librarian);
        }


        User staffUserAccount = userRepo.findByUserRole(UserRole.STAFF);
        if (staffUserAccount == null) {
            User normalUser = new User();
            normalUser.setEmail("staff@test.com");
            normalUser.setName("Default User");
            normalUser.setPassword(bCryptPasswordEncoder.encode("staff"));
            normalUser.setUserRole(UserRole.STAFF);
            userRepo.save(normalUser);
        }

    } catch (Exception e) {
        throw new UserApiException("Failed to create default accounts during initialization");
    }
}


}
