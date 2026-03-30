package com.loopers.application.user;

public interface PasswordEncryptor {
    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
