package com.loopers.application.product;

import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductStatus;

import java.time.ZonedDateTime;

public record ProductResult(
        Long id,
        Long brandId,
        String brandName,
        String name,
        String description,
        int basePrice,
        int likeCount,
        ProductStatus status,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) {
    public static ProductResult from(ProductEntity product, String brandName) {
        return new ProductResult(
                product.getId(),
                product.getBrandId(),
                brandName,
                product.getName(),
                product.getDescription(),
                product.getBasePrice(),
                product.getLikeCount(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
