package com.loopers.domain.member;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberEntity signUp(MemberCommand.CreateMember command) {
        if(memberRepository.find(command.loginId()).isPresent()) {
            throw new CoreException(ErrorType.CONFLICT);
        }
        if(command.password().length() < 8 || command.password().length() > 16) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
        if(!command.password().matches("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{}|;:',.<>?/~`\"\\\\]+$")) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
        if(command.password().contains(command.birthDate())) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
        MemberEntity memberEntity = MemberEntity.create(command);
        return memberRepository.save(memberEntity);
    }
}
