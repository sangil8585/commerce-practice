package com.loopers.interfaces.api.member;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Member V1 API", description = "Member Controller 스펙입니다.")
public interface MemberV1ApiSpec {

    @Operation(
        summary = "회원 가입",
        description = "회원 가입 요청을 보냅니다."
    )
    ApiResponse<MemberV1Dto.SignupResponse> signup(
        @Schema(name = "유저 ID", description = "요청할 유저 ID")
        MemberV1Dto.SignupRequest signupRequest
    );
}
