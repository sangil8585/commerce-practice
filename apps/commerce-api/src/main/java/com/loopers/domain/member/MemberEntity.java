package com.loopers.domain.member;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends BaseEntity {
    private String loginId;
    private String password;
    private String name;
    private String birthDate;
    private String email;

    public MemberEntity(String loginId, String password, String name, String birthDate, String email) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
    }

    public static MemberEntity create(MemberCommand.CreateMember command) {
        return new MemberEntity(
                command.loginId(),
                command.password(),
                command.name(),
                command.birthDate(),
                command.email()
        );
    }
}
