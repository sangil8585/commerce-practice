package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductEntity create(ProductCommand.CreateProduct command) {
        ProductEntity product = ProductEntity.create(command);
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public ProductEntity getProduct(Long id) {
        return productRepository.find(id)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public ProductEntity getVisibleProduct(Long id) {
        ProductEntity product = getProduct(id);
        if (!product.isVisibleToCustomer() || product.isDeleted()) {
            throw new CoreException(ErrorType.NOT_FOUND);
        }
        return product;
    }

    @Transactional(readOnly = true)
    public Page<ProductEntity> getVisibleProducts(Long brandId, Pageable pageable) {
        return productRepository.findVisibleProducts(brandId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProductEntity> getProductsForAdmin(Long brandId, Pageable pageable) {
        return productRepository.findAllForAdmin(brandId, pageable);
    }

    @Transactional
    public ProductEntity update(Long id, ProductCommand.UpdateProduct command) {
        ProductEntity product = getProduct(id);
        product.update(command);
        return product;
    }

    @Transactional
    public ProductEntity delete(Long id) {
        ProductEntity product = getProduct(id);
        if (product.isDeleted()) {
            throw new CoreException(ErrorType.CONFLICT, "이미 삭제된 상품입니다.");
        }
        product.delete();
        return product;
    }

    @Transactional
    public List<Long> softDeleteAllByBrandId(Long brandId) {
        return productRepository.softDeleteAllByBrandId(brandId);
    }
}
