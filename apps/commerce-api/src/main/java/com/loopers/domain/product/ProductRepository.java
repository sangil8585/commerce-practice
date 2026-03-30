package com.loopers.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<ProductEntity> find(Long id);
    ProductEntity save(ProductEntity product);
    Page<ProductEntity> findVisibleProducts(Long brandId, Pageable pageable);
    Page<ProductEntity> findAllForAdmin(Long brandId, Pageable pageable);
    List<Long> softDeleteAllByBrandId(Long brandId);
}
