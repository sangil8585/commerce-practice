package com.loopers.application.brand;

import com.loopers.domain.brand.BrandCommand;
import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.brand.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandFacade {

    private final BrandService brandService;

    public BrandResult getBrand(Long id) {
        BrandEntity brand = brandService.getActiveBrand(id);
        return BrandResult.from(brand);
    }

    public Page<BrandResult> getBrands(Pageable pageable) {
        return brandService.getActiveBrands(pageable).map(BrandResult::from);
    }

    public BrandResult createBrand(BrandCommand.CreateBrand command) {
        BrandEntity brand = brandService.create(command);
        return BrandResult.from(brand);
    }

    public BrandResult getBrandForAdmin(Long id) {
        BrandEntity brand = brandService.getBrand(id);
        return BrandResult.from(brand);
    }

    public Page<BrandResult> getBrandsForAdmin(Pageable pageable) {
        return brandService.getBrands(pageable).map(BrandResult::from);
    }

    public BrandResult updateBrand(Long id, BrandCommand.UpdateBrand command) {
        BrandEntity brand = brandService.update(id, command);
        return BrandResult.from(brand);
    }

    public BrandResult deleteBrand(Long id) {
        BrandEntity brand = brandService.delete(id);
        // TODO: Product 도메인 구현 후 소속 상품 + 재고 연쇄 soft delete 추가
        return BrandResult.from(brand);
    }
}
