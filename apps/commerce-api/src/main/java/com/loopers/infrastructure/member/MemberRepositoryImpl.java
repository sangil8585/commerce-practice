package com.loopers.infrastructure.member;

import com.loopers.domain.member.MemberEntity;
import com.loopers.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<MemberEntity> find(String loginId) {
        return memberJpaRepository.findByLoginId(loginId);
    }

    @Override
    public MemberEntity save(MemberEntity memberEntity) {
        return memberJpaRepository.save(memberEntity);
    }
}
