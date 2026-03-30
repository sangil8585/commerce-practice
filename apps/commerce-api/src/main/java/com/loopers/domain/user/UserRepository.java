package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> find(String loginId);
    UserEntity save(UserEntity user);
}
