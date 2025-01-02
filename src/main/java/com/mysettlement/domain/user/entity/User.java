package com.mysettlement.domain.user.entity;

import com.mysettlement.domain.user.dto.request.UserSigninRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "USERS")
@NoArgsConstructor(access = PROTECTED)
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    @Column(name = "user_role")
    @Enumerated(STRING)
    private UserRole userRole;


    @Builder
    private User(String name, String email, String password, UserRole userRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public static User of(UserSigninRequestDto dto) {
        return User.builder()
                   .name(dto.getName())
                   .email(dto.getEmail())
                   .password(dto.getPassword())
                   .userRole(UserRole.GUEST)
                   .build();
    }

    public void update(UserRole userRole) {
        this.userRole = userRole;
    }
}
