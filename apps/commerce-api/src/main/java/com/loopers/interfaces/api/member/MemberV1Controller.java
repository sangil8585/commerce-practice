package com.loopers.interfaces.api.member;

import com.loopers.domain.member.MemberCommand;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberV1Controller implements MemberV1ApiSpec{

    @PostMapping("/signup")
    @Override
    public ApiResponse<MemberV1Dto.SignupResponse> signup(
        @RequestBody MemberV1Dto.SignupRequest signupRequest
    ) {
        MemberCommand.CreateMember command = signupRequest.toCommand();
        return ApiResponse.success();
    }

}
