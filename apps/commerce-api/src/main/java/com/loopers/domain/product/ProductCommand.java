package com.loopers.domain.product;

public class ProductCommand {

    public record CreateProduct(
            Long brandId,
            String name,
            String description,
            int basePrice
    ) {
    }

    public record UpdateProduct(
            String name,
            String description,
            int basePrice
    ) {
    }
}
