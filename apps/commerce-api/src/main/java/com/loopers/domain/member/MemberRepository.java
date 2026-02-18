package com.loopers.domain.member;

public interface MemberRepository {
    boolean exists(String loginId);
    MemberEntity save(MemberEntity member);
}
