package com.loopers.interfaces.api.brand;

import com.loopers.application.brand.BrandResult;
import com.loopers.domain.brand.BrandCommand;
import com.loopers.domain.brand.BrandStatus;

import java.time.ZonedDateTime;

public class BrandV1Dto {

    // ── 고객용 응답 (상태/관리정보 제외) ──
    public record BrandResponse(
            Long id,
            String name,
            String description
    ) {
        public static BrandResponse from(BrandResult result) {
            return new BrandResponse(
                    result.id(),
                    result.name(),
                    result.description()
            );
        }
    }

    // ── 어드민 요청 ──
    public record CreateBrandRequest(
            String name,
            String description
    ) {
        public BrandCommand.CreateBrand toCommand() {
            return new BrandCommand.CreateBrand(name, description);
        }
    }

    public record UpdateBrandRequest(
            String name,
            String description
    ) {
        public BrandCommand.UpdateBrand toCommand() {
            return new BrandCommand.UpdateBrand(name, description);
        }
    }

    // ── 어드민용 응답 (모든 필드 포함) ──
    public record BrandAdminResponse(
            Long id,
            String name,
            String description,
            BrandStatus status,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt
    ) {
        public static BrandAdminResponse from(BrandResult result) {
            return new BrandAdminResponse(
                    result.id(),
                    result.name(),
                    result.description(),
                    result.status(),
                    result.createdAt(),
                    result.updatedAt()
            );
        }
    }
}
