package com.loopers.domain.member;

import java.util.Optional;

public interface MemberRepository {
    Optional<MemberEntity> find(String loginId);
    MemberEntity save(MemberEntity member);
}
