package com.loopers.domain.brand;

public class BrandCommand {

    public record CreateBrand(
            String name,
            String description
    ) {
    }

    public record UpdateBrand(
            String name,
            String description
    ) {
    }
}
