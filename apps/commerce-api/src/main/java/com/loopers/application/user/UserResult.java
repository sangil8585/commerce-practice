package com.loopers.application.user;

import com.loopers.domain.user.UserEntity;

public record UserResult(
        String loginId,
        String name,
        String birthDate,
        String email
) {
    public static UserResult from(UserEntity user) {
        return new UserResult(
                user.getLoginId(),
                user.getName(),
                user.getBirthDate(),
                user.getEmail()
        );
    }
}
