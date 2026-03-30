package com.loopers.application.brand;

import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.brand.BrandStatus;

import java.time.ZonedDateTime;

public record BrandResult(
        Long id,
        String name,
        String description,
        BrandStatus status,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) {
    public static BrandResult from(BrandEntity brand) {
        return new BrandResult(
                brand.getId(),
                brand.getName(),
                brand.getDescription(),
                brand.getStatus(),
                brand.getCreatedAt(),
                brand.getUpdatedAt()
        );
    }
}
