package com.loopers.interfaces.api.member;

import com.loopers.application.member.MemberResult;
import com.loopers.domain.member.MemberCommand;

public class MemberV1Dto {
    public record SignupRequest(
            String loginId,
            String password,
            String name,
            String birthDate,
            String email
    ) {
        public MemberCommand.CreateMember toCommand() {
            return new MemberCommand.CreateMember(
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
        public static SignupResponse from(MemberResult result) {
            return new SignupResponse(
                    result.loginId(),
                    result.name(),
                    result.birthDate(),
                    result.email()
            );
        }
    }

    public record MemberResponse(
            String loginId,
            String name,
            String birthDate,
            String email
    ) {
        public static MemberResponse from(MemberResult result) {
            return new MemberResponse(
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
