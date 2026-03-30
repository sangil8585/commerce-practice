package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private static final List<ProductStatus> VISIBLE_STATUSES = List.of(ProductStatus.ACTIVE, ProductStatus.SOLDOUT);

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Optional<ProductEntity> find(Long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public ProductEntity save(ProductEntity product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Page<ProductEntity> findVisibleProducts(Long brandId, Pageable pageable) {
        if (brandId != null) {
            return productJpaRepository.findByBrandIdAndStatusInAndDeletedAtIsNull(brandId, VISIBLE_STATUSES, pageable);
        }
        return productJpaRepository.findByStatusInAndDeletedAtIsNull(VISIBLE_STATUSES, pageable);
    }

    @Override
    public Page<ProductEntity> findAllForAdmin(Long brandId, Pageable pageable) {
        if (brandId != null) {
            return productJpaRepository.findByBrandIdAndDeletedAtIsNull(brandId, pageable);
        }
        return productJpaRepository.findByDeletedAtIsNull(pageable);
    }

    @Override
    public List<Long> softDeleteAllByBrandId(Long brandId) {
        List<Long> productIds = productJpaRepository.findIdsByBrandIdAndDeletedAtIsNull(brandId);
        productJpaRepository.softDeleteAllByBrandId(brandId);
        return productIds;
    }
}
