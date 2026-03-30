package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends BaseEntity {

    @Column(name = "brand_id", nullable = false)
    private Long brandId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "base_price", nullable = false)
    private int basePrice;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status;

    private ProductEntity(Long brandId, String name, String description, int basePrice) {
        this.brandId = brandId;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.likeCount = 0;
        this.status = ProductStatus.ACTIVE;
    }

    public static ProductEntity create(ProductCommand.CreateProduct command) {
        return new ProductEntity(
                command.brandId(),
                command.name(),
                command.description(),
                command.basePrice()
        );
    }

    public void update(ProductCommand.UpdateProduct command) {
        this.name = command.name();
        this.description = command.description();
        this.basePrice = command.basePrice();
    }

    public boolean isVisibleToCustomer() {
        return this.status == ProductStatus.ACTIVE || this.status == ProductStatus.SOLDOUT;
    }

    public boolean isDeleted() {
        return getDeletedAt() != null;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
