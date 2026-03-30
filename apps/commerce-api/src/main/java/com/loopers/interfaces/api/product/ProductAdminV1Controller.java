package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductResult;
import com.loopers.domain.product.ProductCommand;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api-admin/v1/products")
public class ProductAdminV1Controller implements ProductAdminV1ApiSpec {

    private static final String ADMIN_LDAP = "loopers.admin";

    private final ProductFacade productFacade;

    @GetMapping
    @Override
    public ApiResponse<Page<ProductV1Dto.ProductAdminResponse>> getProducts(
            @RequestParam(required = false) Long brandId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ProductV1Dto.ProductAdminResponse> response = productFacade.getProductsForAdmin(brandId, pageRequest)
                .map(ProductV1Dto.ProductAdminResponse::from);
        return ApiResponse.success(response);
    }

    @GetMapping("/{productId}")
    @Override
    public ApiResponse<ProductV1Dto.ProductAdminResponse> getProduct(@PathVariable Long productId) {
        ProductResult result = productFacade.getProductForAdmin(productId);
        ProductV1Dto.ProductAdminResponse response = ProductV1Dto.ProductAdminResponse.from(result);
        return ApiResponse.success(response);
    }

    @PostMapping
    @Override
    public ApiResponse<ProductV1Dto.ProductAdminResponse> createProduct(
            @RequestHeader("X-Loopers-Ldap") String ldap,
            @RequestBody ProductV1Dto.CreateProductRequest request
    ) {
        validateAdmin(ldap);
        ProductCommand.CreateProduct command = request.toCommand();
        ProductResult result = productFacade.createProduct(command);
        ProductV1Dto.ProductAdminResponse response = ProductV1Dto.ProductAdminResponse.from(result);
        return ApiResponse.success(response);
    }

    @PutMapping("/{productId}")
    @Override
    public ApiResponse<ProductV1Dto.ProductAdminResponse> updateProduct(
            @RequestHeader("X-Loopers-Ldap") String ldap,
            @PathVariable Long productId,
            @RequestBody ProductV1Dto.UpdateProductRequest request
    ) {
        validateAdmin(ldap);
        ProductCommand.UpdateProduct command = request.toCommand();
        ProductResult result = productFacade.updateProduct(productId, command);
        ProductV1Dto.ProductAdminResponse response = ProductV1Dto.ProductAdminResponse.from(result);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{productId}")
    @Override
    public ApiResponse<ProductV1Dto.ProductAdminResponse> deleteProduct(
            @RequestHeader("X-Loopers-Ldap") String ldap,
            @PathVariable Long productId
    ) {
        validateAdmin(ldap);
        ProductResult result = productFacade.deleteProduct(productId);
        ProductV1Dto.ProductAdminResponse response = ProductV1Dto.ProductAdminResponse.from(result);
        return ApiResponse.success(response);
    }

    private void validateAdmin(String ldap) {
        if (!ADMIN_LDAP.equals(ldap)) {
            throw new CoreException(ErrorType.UNAUTHORIZED, "어드민 권한이 없습니다.");
        }
    }
}
