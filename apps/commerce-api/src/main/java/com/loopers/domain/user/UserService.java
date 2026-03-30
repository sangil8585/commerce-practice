package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserEntity signUp(UserCommand.CreateUser command, String encodedPassword) {
        if(userRepository.find(command.loginId()).isPresent()) {
            throw new CoreException(ErrorType.CONFLICT);
        }
        validatePassword(command.password(), command.birthDate());
        UserEntity userEntity = UserEntity.create(command, encodedPassword);
        return userRepository.save(userEntity);
    }

    @Transactional
    public void changePassword(UserEntity user, String encodedPassword) {
        user.changePassword(encodedPassword);
    }

    @Transactional(readOnly = true)
    public UserEntity getUser(String loginId) {
        return userRepository.find(loginId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
    }

    private void validatePassword(String password, String birthDate) {
        if(password.length() < 9 || password.length() > 16) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
        if(!password.matches("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{}|;:',.<>?/~`\"\\\\]+$")) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
        if(!password.matches(".*[a-zA-Z].*")) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
        if(!password.matches(".*[0-9].*")) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
        if(!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;:',.<>?/~`\"\\\\].*")) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
        if(password.contains(birthDate)) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
    }
}
