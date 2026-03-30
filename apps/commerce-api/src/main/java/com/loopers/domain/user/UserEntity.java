package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {
    private String loginId;
    private String password;
    private String name;
    private String birthDate;
    private String email;

    public UserEntity(String loginId, String password, String name, String birthDate, String email) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
    }

    public static UserEntity create(UserCommand.CreateUser command, String encodedPassword) {
        return new UserEntity(
                command.loginId(),
                encodedPassword,
                command.name(),
                command.birthDate(),
                command.email()
        );
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
