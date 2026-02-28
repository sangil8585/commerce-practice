package com.loopers.interfaces.api.member;

import com.loopers.domain.member.MemberEntity;
import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Member V1 API", description = "Member Controller 스펙입니다.")
public interface MemberV1ApiSpec {

    @Operation(summary = "회원 가입", description = "회원 가입 요청을 보냅니다.")
    ApiResponse<MemberV1Dto.SignupResponse> signup(
            MemberV1Dto.SignupRequest signupRequest
    );

    @Operation(summary = "정보 조회", description = "유저 정보를 조회합니다.")
    ApiResponse<MemberV1Dto.MemberResponse> getMember(
            @RequestHeader("X-Loopers-LoginId") String loginId,
            @RequestHeader("X-Loopers-LoginPw") String password
    );

    @Operation(summary = "비밀번호 수정", description = "비밀번호를 변경합니다.")
    ApiResponse<Void> changePassword(
            @RequestHeader("X-Loopers-LoginId") String loginId,
            @RequestHeader("X-Loopers-LoginPw") String password,
            MemberV1Dto.ChangePasswordRequest request
    );
}
