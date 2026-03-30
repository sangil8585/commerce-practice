package com.loopers.application.user;

import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final PasswordEncryptor passwordEncryptor;

    public UserResult createUser(UserCommand.CreateUser command) {
        String encodedPassword = passwordEncryptor.encode(command.password());
        UserEntity userEntity = userService.signUp(command, encodedPassword);
        return UserResult.from(userEntity);
    }

    public UserResult getUser(String loginId, String password) {
        UserEntity user = userService.getUser(loginId);
        if (!passwordEncryptor.matches(password, user.getPassword())) {
            throw new CoreException(ErrorType.UNAUTHORIZED);
        }
        return UserResult.from(user);
    }

    public void changePassword(String loginId, String password, String currentPassword, String newPassword) {
        UserEntity user = userService.getUser(loginId);
        if (!passwordEncryptor.matches(password, user.getPassword())) {
            throw new CoreException(ErrorType.UNAUTHORIZED);
        }
        if (currentPassword.equals(newPassword)) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
        String encodedPassword = passwordEncryptor.encode(newPassword);
        userService.changePassword(user, encodedPassword);
    }
}
