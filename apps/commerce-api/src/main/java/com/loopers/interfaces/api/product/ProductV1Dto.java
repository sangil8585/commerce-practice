package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductResult;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductStatus;

import java.time.ZonedDateTime;

public class ProductV1Dto {

    // ── 고객용 응답 ──

    public record ProductListResponse(
            Long id,
            String name,
            int basePrice,
            String brandName,
            int likeCount,
            boolean soldOut
    ) {
        public static ProductListResponse from(ProductResult result) {
            return new ProductListResponse(
                    result.id(),
                    result.name(),
                    result.basePrice(),
                    result.brandName(),
                    result.likeCount(),
                    result.status() == ProductStatus.SOLDOUT
            );
        }
    }

    public record ProductDetailResponse(
            Long id,
            String name,
            String description,
            int basePrice,
            String brandName,
            int likeCount,
            boolean soldOut
    ) {
        public static ProductDetailResponse from(ProductResult result) {
            return new ProductDetailResponse(
                    result.id(),
                    result.name(),
                    result.description(),
                    result.basePrice(),
                    result.brandName(),
                    result.likeCount(),
                    result.status() == ProductStatus.SOLDOUT
            );
        }
    }

    // ── 어드민 요청 ──

    public record CreateProductRequest(
            Long brandId,
            String name,
            String description,
            int basePrice
    ) {
        public ProductCommand.CreateProduct toCommand() {
            return new ProductCommand.CreateProduct(brandId, name, description, basePrice);
        }
    }

    public record UpdateProductRequest(
            String name,
            String description,
            int basePrice
    ) {
        public ProductCommand.UpdateProduct toCommand() {
            return new ProductCommand.UpdateProduct(name, description, basePrice);
        }
    }

    // ── 어드민용 응답 ──

    public record ProductAdminResponse(
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
        public static ProductAdminResponse from(ProductResult result) {
            return new ProductAdminResponse(
                    result.id(),
                    result.brandId(),
                    result.brandName(),
                    result.name(),
                    result.description(),
                    result.basePrice(),
                    result.likeCount(),
                    result.status(),
                    result.createdAt(),
                    result.updatedAt()
            );
        }
    }
}
