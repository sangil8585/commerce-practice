package com.loopers.interfaces.api.brand;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;

@Tag(name = "Brand V1 API", description = "고객용 브랜드 API 스펙입니다.")
public interface BrandV1ApiSpec {

    @Operation(summary = "브랜드 목록 조회", description = "ACTIVE 브랜드 목록을 조회합니다.")
    ApiResponse<Page<BrandV1Dto.BrandResponse>> getBrands(int page, int size);

    @Operation(summary = "브랜드 조회", description = "ACTIVE 브랜드 정보를 조회합니다.")
    ApiResponse<BrandV1Dto.BrandResponse> getBrand(Long brandId);
}
