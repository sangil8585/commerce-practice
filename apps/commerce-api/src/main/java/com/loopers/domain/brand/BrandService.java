package com.loopers.domain.brand;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BrandService {

    private final BrandRepository brandRepository;

    @Transactional
    public BrandEntity create(BrandCommand.CreateBrand command) {
        BrandEntity brand = BrandEntity.create(command);
        return brandRepository.save(brand);
    }

    @Transactional(readOnly = true)
    public BrandEntity getBrand(Long id) {
        return brandRepository.find(id)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public BrandEntity getActiveBrand(Long id) {
        BrandEntity brand = getBrand(id);
        if (!brand.isActive() || brand.isDeleted()) {
            throw new CoreException(ErrorType.NOT_FOUND);
        }
        return brand;
    }

    @Transactional(readOnly = true)
    public Page<BrandEntity> getBrands(Pageable pageable) {
        return brandRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<BrandEntity> getActiveBrands(Pageable pageable) {
        return brandRepository.findAllActive(pageable);
    }

    @Transactional
    public BrandEntity update(Long id, BrandCommand.UpdateBrand command) {
        BrandEntity brand = getBrand(id);
        brand.update(command);
        return brand;
    }

    @Transactional
    public BrandEntity delete(Long id) {
        BrandEntity brand = getBrand(id);
        if (brand.isDeleted()) {
            throw new CoreException(ErrorType.CONFLICT, "이미 삭제된 브랜드입니다.");
        }
        brand.delete();
        return brand;
    }
}
