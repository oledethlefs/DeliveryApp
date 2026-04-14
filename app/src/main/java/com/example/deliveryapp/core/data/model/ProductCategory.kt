package com.example.deliveryapp.core.data.model


/**
 * Represents a category of products.
 *
 * This data class holds the essential information for a product category,
 * including its unique identifier, display name, and a URL for its image.
 *
 * @property id The unique identifier for the product category.
 * @property name The name of the product category (e.g., "Beverages", "Fruits & Vegetables").
 * @property imageUrl The URL pointing to an image representing this category.
 * @property childrenProductCategories A list of nested sub-categories belonging to this category.
 * @property productFilters A list of [ProductFilter] associated with this category.
 */
data class ProductCategory (
    val id: Int,
    val name: String,
    val imageUrl: String,
    val childrenProductCategories: List<ProductCategory>,
    val parentCategoryId: Int? = null,
    val productFilters: List<ProductFilter>,
)