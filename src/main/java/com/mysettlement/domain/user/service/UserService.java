package com.mysettlement.domain.user.service;

import com.mysettlement.domain.user.dto.request.UserSigninRequestDto;
import com.mysettlement.domain.user.dto.response.UserResponseDto;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.entity.UserRole;
import com.mysettlement.domain.user.exception.DuplicateUserException;
import com.mysettlement.domain.user.exception.NoUserFoundException;
import com.mysettlement.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;


    public UserResponseDto findByUserName(String name) {
        User user = userRepository.findByName(name).orElseThrow(NoUserFoundException::new);
        return UserResponseDto.of(user);
    }

    @Transactional
    public UserResponseDto signinUser(UserSigninRequestDto userSigninRequestDto) {
        if (isExistUser(userSigninRequestDto)) {
            throw new DuplicateUserException();
        }
        User newUser = User.of(userSigninRequestDto);
        userRepository.save(newUser);
        return UserResponseDto.of(newUser);
    }

    @Transactional
    public UserResponseDto changeUserStatus(Long userId, UserRole userRole) {
        User foundUser = userRepository.findById(userId).orElseThrow(NoUserFoundException::new);
        foundUser.update(userRole);
        return UserResponseDto.of(foundUser);
    }

    private boolean isExistUser(UserSigninRequestDto userSigninRequestDto) {
        return userRepository.findByEmail(userSigninRequestDto.getEmail()).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                             .orElseThrow(NoUserFoundException::new);
    }
}
