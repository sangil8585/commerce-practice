package com.loopers.interfaces.api.product;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;

@Tag(name = "Product Admin V1 API", description = "어드민용 상품 관리 API 스펙입니다.")
public interface ProductAdminV1ApiSpec {

    @Operation(summary = "상품 목록 조회", description = "모든 상태의 상품을 조회합니다.")
    ApiResponse<Page<ProductV1Dto.ProductAdminResponse>> getProducts(Long brandId, int page, int size);

    @Operation(summary = "상품 상세 조회", description = "상품 상세 정보를 조회합니다.")
    ApiResponse<ProductV1Dto.ProductAdminResponse> getProduct(Long productId);

    @Operation(summary = "상품 등록", description = "새 상품을 등록합니다. 브랜드가 ACTIVE여야 합니다.")
    ApiResponse<ProductV1Dto.ProductAdminResponse> createProduct(
            String ldap, ProductV1Dto.CreateProductRequest request
    );

    @Operation(summary = "상품 수정", description = "상품 정보를 수정합니다. brand_id는 수정 불가합니다.")
    ApiResponse<ProductV1Dto.ProductAdminResponse> updateProduct(
            String ldap, Long productId, ProductV1Dto.UpdateProductRequest request
    );

    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다. 재고도 함께 삭제됩니다.")
    ApiResponse<ProductV1Dto.ProductAdminResponse> deleteProduct(String ldap, Long productId);
}
