package com.loopers.domain.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BrandRepository {
    Optional<BrandEntity> find(Long id);
    BrandEntity save(BrandEntity brand);
    Page<BrandEntity> findAll(Pageable pageable);
    Page<BrandEntity> findAllActive(Pageable pageable);
}
