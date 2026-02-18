package com.loopers.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberEntity signUp(MemberCommand.CreateMember command) {
        MemberEntity memberEntity = MemberEntity.create(command);
        return memberRepository.save(memberEntity);
    }
}
