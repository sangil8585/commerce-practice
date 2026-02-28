package com.loopers.interfaces.api.member;

import com.loopers.application.member.MemberFacade;
import com.loopers.application.member.MemberResult;
import com.loopers.domain.member.MemberCommand;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberV1Controller implements MemberV1ApiSpec{

    private final MemberFacade memberFacade;

    @PostMapping("/signup")
    @Override
    public ApiResponse<MemberV1Dto.SignupResponse> signup(
        @RequestBody MemberV1Dto.SignupRequest signupRequest
    ) {
        MemberCommand.CreateMember command = signupRequest.toCommand();
        MemberResult result = memberFacade.createMember(command);
        MemberV1Dto.SignupResponse response = MemberV1Dto.SignupResponse.from(result);

        return ApiResponse.success(response);
    }

    @GetMapping("/me")
    @Override
    public ApiResponse<MemberV1Dto.MemberResponse> getMember(
            @RequestHeader(value = "X-Loopers-LoginId", required = false) String loginId,
            @RequestHeader(value = "X-Loopers-LoginPw", required = false) String password
    ) {
        MemberResult result = memberFacade.getMember(loginId, password);
        MemberV1Dto.MemberResponse response = MemberV1Dto.MemberResponse.from(result);
        return ApiResponse.success(response);
    }

    @PatchMapping("/me/password")
    @Override
    public ApiResponse<Void> changePassword(
            @RequestHeader(value = "X-Loopers-LoginId", required = false) String loginId,
            @RequestHeader(value = "X-Loopers-LoginPw", required = false) String password,
            @RequestBody MemberV1Dto.ChangePasswordRequest request
    ) {
        memberFacade.changePassword(loginId, password, request.currentPassword(), request.newPassword());
        return ApiResponse.success(null);
    }
}
