package com.loopers.interfaces.api.brand;

import com.loopers.application.brand.BrandFacade;
import com.loopers.application.brand.BrandResult;
import com.loopers.domain.brand.BrandCommand;
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
@RequestMapping("/api-admin/v1/brands")
public class BrandAdminV1Controller implements BrandAdminV1ApiSpec {

    private static final String ADMIN_LDAP = "loopers.admin";

    private final BrandFacade brandFacade;

    @GetMapping
    @Override
    public ApiResponse<Page<BrandV1Dto.BrandAdminResponse>> getBrands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BrandV1Dto.BrandAdminResponse> response = brandFacade.getBrandsForAdmin(pageRequest)
                .map(BrandV1Dto.BrandAdminResponse::from);
        return ApiResponse.success(response);
    }

    @GetMapping("/{brandId}")
    @Override
    public ApiResponse<BrandV1Dto.BrandAdminResponse> getBrand(@PathVariable Long brandId) {
        BrandResult result = brandFacade.getBrandForAdmin(brandId);
        BrandV1Dto.BrandAdminResponse response = BrandV1Dto.BrandAdminResponse.from(result);
        return ApiResponse.success(response);
    }

    @PostMapping
    @Override
    public ApiResponse<BrandV1Dto.BrandAdminResponse> createBrand(
            @RequestHeader("X-Loopers-Ldap") String ldap,
            @RequestBody BrandV1Dto.CreateBrandRequest request
    ) {
        validateAdmin(ldap);
        BrandCommand.CreateBrand command = request.toCommand();
        BrandResult result = brandFacade.createBrand(command);
        BrandV1Dto.BrandAdminResponse response = BrandV1Dto.BrandAdminResponse.from(result);
        return ApiResponse.success(response);
    }

    @PutMapping("/{brandId}")
    @Override
    public ApiResponse<BrandV1Dto.BrandAdminResponse> updateBrand(
            @RequestHeader("X-Loopers-Ldap") String ldap,
            @PathVariable Long brandId,
            @RequestBody BrandV1Dto.UpdateBrandRequest request
    ) {
        validateAdmin(ldap);
        BrandCommand.UpdateBrand command = request.toCommand();
        BrandResult result = brandFacade.updateBrand(brandId, command);
        BrandV1Dto.BrandAdminResponse response = BrandV1Dto.BrandAdminResponse.from(result);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{brandId}")
    @Override
    public ApiResponse<BrandV1Dto.BrandAdminResponse> deleteBrand(
            @RequestHeader("X-Loopers-Ldap") String ldap,
            @PathVariable Long brandId
    ) {
        validateAdmin(ldap);
        BrandResult result = brandFacade.deleteBrand(brandId);
        BrandV1Dto.BrandAdminResponse response = BrandV1Dto.BrandAdminResponse.from(result);
        return ApiResponse.success(response);
    }

    private void validateAdmin(String ldap) {
        if (!ADMIN_LDAP.equals(ldap)) {
            throw new CoreException(ErrorType.UNAUTHORIZED, "어드민 권한이 없습니다.");
        }
    }
}
