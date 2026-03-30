package com.loopers.domain.user;

public class UserCommand {
    public record CreateUser(
            String loginId,
            String password,
            String name,
            String birthDate,
            String email
    ) {

    }
}
