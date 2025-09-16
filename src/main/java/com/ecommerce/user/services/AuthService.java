package com.ecommerce.user.services;

import com.ecommerce.user.dto.SignupRequest;
import com.ecommerce.user.dto.UserDto;
import com.ecommerce.user.entity.User;

import java.util.Optional;

public interface AuthService {
    UserDto createUser(SignupRequest signupRequest) ;
    UserDto createAdminAccount(SignupRequest signupRequest);
    UserDto createStaffAccount(SignupRequest signupRequest);

    UserDto createLibrarianAccount(SignupRequest signupRequest);
    Boolean hasUserWithEmail(String email);

    Optional<User> getUserByEmail(String email);

    Optional<User> getCurrentUser(String token);
}
