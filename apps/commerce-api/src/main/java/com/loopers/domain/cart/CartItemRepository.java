package com.loopers.domain.cart;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository {
    Optional<CartItemEntity> find(Long id);
    Optional<CartItemEntity> findByUserIdAndProductId(Long userId, Long productId);
    List<CartItemEntity> findByUserId(Long userId);
    CartItemEntity save(CartItemEntity cartItem);
}
