package com.loopers.application.member;

import com.loopers.domain.member.MemberEntity;

public record MemberResult(
        String loginId,
        String name,
        String birthDate,
        String email
) {
    public static MemberResult from(MemberEntity member) {
        return new MemberResult(
                member.getLoginId(),
                member.getName(),
                member.getBirthDate(),
                member.getEmail()
        );
    }
}
