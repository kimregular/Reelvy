package com.mysettlement.domain.user.handler;

import com.mysettlement.domain.user.dto.request.UserSignUpRequest;
import com.mysettlement.domain.user.entity.User;
import com.mysettlement.domain.user.handler.UserBuildHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserBuildHandlerTest {

    private UserBuildHandler userBuildHandler;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        userBuildHandler = new UserBuildHandler(passwordEncoder);
    }

    @Test
    @DisplayName("유저 엔티티를 생성한다.")
    void testBuildUserWith_shouldReturnValidUser() {
        // given
        UserSignUpRequest request = new UserSignUpRequest("user1", "nick1", "rawPass");
        when(passwordEncoder.encode("rawPass")).thenReturn("encodedPass");

        // when
        User user = userBuildHandler.buildUserWith(request);

        // then
        assertEquals("user1", user.getUsername());
        assertEquals("nick1", user.getNickname());
        assertEquals("encodedPass", user.getPassword());
    }
}