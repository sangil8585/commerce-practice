package com.loopers.domain.brand;

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
@Table(name = "brands")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandEntity extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BrandStatus status;

    private BrandEntity(String name, String description, BrandStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public static BrandEntity create(BrandCommand.CreateBrand command) {
        return new BrandEntity(command.name(), command.description(), BrandStatus.ACTIVE);
    }

    public void update(BrandCommand.UpdateBrand command) {
        this.name = command.name();
        this.description = command.description();
    }

    public void changeStatus(BrandStatus status) {
        this.status = status;
    }

    public boolean isActive() {
        return this.status == BrandStatus.ACTIVE;
    }

    public boolean isDeleted() {
        return getDeletedAt() != null;
    }
}
