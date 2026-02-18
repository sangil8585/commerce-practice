package com.loopers.interfaces.api.member;

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
            String email
    ) {

    }

    public record MeResponse(
            String loginId,
            String name,
            String birthDate,
            String email
    ) {

    }

    public record ChangePasswordRequest(
            String loginId,
            String password
    ) {

    }
}
