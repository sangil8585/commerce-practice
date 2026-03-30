package com.loopers.application.product;

import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductService productService;
    private final BrandService brandService;

    // ── 고객용 ──

    public ProductResult getProduct(Long productId) {
        ProductEntity product = productService.getVisibleProduct(productId);
        String brandName = brandService.getBrand(product.getBrandId()).getName();
        return ProductResult.from(product, brandName);
    }

    public Page<ProductResult> getProducts(Long brandId, Pageable pageable) {
        Page<ProductEntity> products = productService.getVisibleProducts(brandId, pageable);
        Map<Long, String> brandNameMap = resolveBrandNames(products.getContent());
        return products.map(p -> ProductResult.from(p, brandNameMap.get(p.getBrandId())));
    }

    // ── 어드민용 ──

    public ProductResult getProductForAdmin(Long productId) {
        ProductEntity product = productService.getProduct(productId);
        String brandName = brandService.getBrand(product.getBrandId()).getName();
        return ProductResult.from(product, brandName);
    }

    public Page<ProductResult> getProductsForAdmin(Long brandId, Pageable pageable) {
        Page<ProductEntity> products = productService.getProductsForAdmin(brandId, pageable);
        Map<Long, String> brandNameMap = resolveBrandNames(products.getContent());
        return products.map(p -> ProductResult.from(p, brandNameMap.get(p.getBrandId())));
    }

    public ProductResult createProduct(ProductCommand.CreateProduct command) {
        BrandEntity brand = brandService.getBrand(command.brandId());
        if (!brand.isActive()) {
            throw new CoreException(ErrorType.CONFLICT, "비활성 브랜드에는 상품을 등록할 수 없습니다.");
        }
        ProductEntity product = productService.create(command);
        return ProductResult.from(product, brand.getName());
    }

    public ProductResult updateProduct(Long productId, ProductCommand.UpdateProduct command) {
        ProductEntity product = productService.update(productId, command);
        String brandName = brandService.getBrand(product.getBrandId()).getName();
        return ProductResult.from(product, brandName);
    }

    public ProductResult deleteProduct(Long productId) {
        ProductEntity product = productService.delete(productId);
        String brandName = brandService.getBrand(product.getBrandId()).getName();
        // TODO: Inventory 도메인 구현 후 재고 soft delete 추가
        return ProductResult.from(product, brandName);
    }

    public List<Long> deleteProductsByBrandId(Long brandId) {
        // TODO: Inventory 도메인 구현 후 반환된 productIds로 재고 연쇄 soft delete 추가
        return productService.softDeleteAllByBrandId(brandId);
    }

    private Map<Long, String> resolveBrandNames(List<ProductEntity> products) {
        Set<Long> brandIds = products.stream()
                .map(ProductEntity::getBrandId)
                .collect(Collectors.toSet());

        Map<Long, String> brandNameMap = new HashMap<>();
        for (Long brandId : brandIds) {
            brandNameMap.put(brandId, brandService.getBrand(brandId).getName());
        }
        return brandNameMap;
    }
}
