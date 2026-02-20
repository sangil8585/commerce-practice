package com.loopers.domain.member;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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
        String encodedPassword = passwordEncoder.encode(command.password());
        MemberEntity memberEntity = MemberEntity.create(command, encodedPassword);
        return memberRepository.save(memberEntity);
    }


    @Transactional(readOnly = true)
    public MemberEntity getMember(String loginId) {
        return memberRepository.find(loginId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
    }

    // getMember 메서드를 범용성있게 사용하기 위해 password 인증 메서드를 분리한다.
    @Transactional(readOnly = true)
    public MemberEntity authenticate(String loginId, String password) {
        MemberEntity member = getMember(loginId);
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CoreException(ErrorType.UNAUTHORIZED);
        }
        return member;
    }
}
