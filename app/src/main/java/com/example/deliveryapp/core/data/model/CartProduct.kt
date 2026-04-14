package com.example.deliveryapp.core.data.model



/**
 * Represents a product item within the user's shopping cart, including its quantity.
 *
 * @property product The [Product] details associated with this cart entry.
 * @property countInCart The quantity of this specific product added to the cart.
 */
data class CartProduct(
    val product: Product,
    val countInCart: Int
)