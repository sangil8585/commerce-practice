package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    Page<ProductEntity> findByStatusInAndDeletedAtIsNull(List<ProductStatus> statuses, Pageable pageable);

    Page<ProductEntity> findByBrandIdAndStatusInAndDeletedAtIsNull(
            Long brandId, List<ProductStatus> statuses, Pageable pageable
    );

    Page<ProductEntity> findByDeletedAtIsNull(Pageable pageable);

    Page<ProductEntity> findByBrandIdAndDeletedAtIsNull(Long brandId, Pageable pageable);

    @Query("SELECT p.id FROM ProductEntity p WHERE p.brandId = :brandId AND p.deletedAt IS NULL")
    List<Long> findIdsByBrandIdAndDeletedAtIsNull(@Param("brandId") Long brandId);

    @Modifying
    @Query("UPDATE ProductEntity p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.brandId = :brandId AND p.deletedAt IS NULL")
    int softDeleteAllByBrandId(@Param("brandId") Long brandId);
}
