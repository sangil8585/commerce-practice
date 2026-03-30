package com.loopers.domain.cart;

public class CartItemCommand {

    public record AddCartItem(
            Long productId,
            int quantity
    ) {
    }

    public record UpdateCartItemQuantity(
            int quantity
    ) {
    }
}
