package com.mysettlement.domain.user.service;

import com.mysettlement.domain.user.dto.request.UserSignupRequestDto;
import com.mysettlement.domain.user.dto.response.UserSignupResponseDto;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.exception.DuplicateUserException;
import com.mysettlement.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserSignupResponseDto signup(UserSignupRequestDto userSignupRequestDto) {
        if (isExistUser(userSignupRequestDto.email())) {
            throw new DuplicateUserException();
        }
        User newUser = User.builder()
                           .email(userSignupRequestDto.email())
                           .password(bCryptPasswordEncoder.encode(userSignupRequestDto.password()))
                           .build();
        userRepository.save(newUser);
        return new UserSignupResponseDto(newUser);
    }

    private boolean isExistUser(String email) {
        return userRepository.findByEmail(email)
                             .isPresent();
    }
}
