package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserResult;
import com.loopers.domain.user.UserCommand;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserV1Controller implements UserV1ApiSpec {

    private final UserFacade userFacade;

    @PostMapping("/signup")
    @Override
    public ApiResponse<UserV1Dto.SignupResponse> signup(
        @RequestBody UserV1Dto.SignupRequest signupRequest
    ) {
        UserCommand.CreateUser command = signupRequest.toCommand();
        UserResult result = userFacade.createUser(command);
        UserV1Dto.SignupResponse response = UserV1Dto.SignupResponse.from(result);

        return ApiResponse.success(response);
    }

    @GetMapping("/me")
    @Override
    public ApiResponse<UserV1Dto.UserResponse> getUser(
            @RequestHeader(value = "X-Loopers-LoginId", required = false) String loginId,
            @RequestHeader(value = "X-Loopers-LoginPw", required = false) String password
    ) {
        UserResult result = userFacade.getUser(loginId, password);
        UserV1Dto.UserResponse response = UserV1Dto.UserResponse.from(result);
        return ApiResponse.success(response);
    }

    @PatchMapping("/me/password")
    @Override
    public ApiResponse<Void> changePassword(
            @RequestHeader(value = "X-Loopers-LoginId", required = false) String loginId,
            @RequestHeader(value = "X-Loopers-LoginPw", required = false) String password,
            @RequestBody UserV1Dto.ChangePasswordRequest request
    ) {
        userFacade.changePassword(loginId, password, request.currentPassword(), request.newPassword());
        return ApiResponse.success(null);
    }
}
