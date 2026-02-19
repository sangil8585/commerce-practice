package com.loopers.application.member;

import com.loopers.domain.member.MemberCommand;
import com.loopers.domain.member.MemberEntity;
import com.loopers.domain.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberFacade {
    private final MemberService memberService;

    public MemberResult createMember(MemberCommand.CreateMember command) {
        MemberEntity memberEntity = memberService.signUp(command);
        return MemberResult.from(memberEntity);
    }
}
