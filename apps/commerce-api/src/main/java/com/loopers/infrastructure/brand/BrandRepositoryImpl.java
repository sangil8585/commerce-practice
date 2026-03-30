package com.loopers.infrastructure.brand;

import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.brand.BrandRepository;
import com.loopers.domain.brand.BrandStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BrandRepositoryImpl implements BrandRepository {

    private final BrandJpaRepository brandJpaRepository;

    @Override
    public Optional<BrandEntity> find(Long id) {
        return brandJpaRepository.findById(id);
    }

    @Override
    public BrandEntity save(BrandEntity brand) {
        return brandJpaRepository.save(brand);
    }

    @Override
    public Page<BrandEntity> findAll(Pageable pageable) {
        return brandJpaRepository.findByDeletedAtIsNull(pageable);
    }

    @Override
    public Page<BrandEntity> findAllActive(Pageable pageable) {
        return brandJpaRepository.findByStatusAndDeletedAtIsNull(BrandStatus.ACTIVE, pageable);
    }
}
