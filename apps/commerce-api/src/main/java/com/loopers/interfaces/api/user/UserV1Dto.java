package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserResult;
import com.loopers.domain.user.UserCommand;

public class UserV1Dto {
    public record SignupRequest(
            String loginId,
            String password,
            String name,
            String birthDate,
            String email
    ) {
        public UserCommand.CreateUser toCommand() {
            return new UserCommand.CreateUser(
                    loginId,
                    password,
                    name,
                    birthDate,
                    email
            );
        }
    }

    public record SignupResponse(
            String loginId,
            String name,
            String birthDate,
            String email
    ) {
        public static SignupResponse from(UserResult result) {
            return new SignupResponse(
                    result.loginId(),
                    result.name(),
                    result.birthDate(),
                    result.email()
            );
        }
    }

    public record UserResponse(
            String loginId,
            String name,
            String birthDate,
            String email
    ) {
        public static UserResponse from(UserResult result) {
            return new UserResponse(
                    result.loginId(),
                    maskName(result.name()),
                    result.birthDate(),
                    result.email()
            );
        }

        private static String maskName(String name) {
            if (name == null || name.isEmpty()) {
                return name;
            }
            return name.substring(0, name.length() - 1) + "*";
        }
    }

    public record ChangePasswordRequest(
            String currentPassword,
            String newPassword
    ) {

    }
}
