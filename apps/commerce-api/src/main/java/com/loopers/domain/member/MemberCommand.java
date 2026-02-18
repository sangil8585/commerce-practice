package com.loopers.domain.member;

public class MemberCommand {
    public record CreateMember(
            String loginId,
            String password,
            String name,
            String birthDate,
            String email
    ) {

    }
}
