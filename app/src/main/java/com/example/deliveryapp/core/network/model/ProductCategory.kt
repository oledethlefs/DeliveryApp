package com.example.deliveryapp.core.network.model

import com.example.deliveryapp.core.data.model.ProductCategory
import kotlinx.serialization.Serializable

/**
 * Represents a product category fetched from the network.
 *
 * This data class models the structure of a product category as defined by the backend API.
 * It is used for deserializing JSON responses from the network.
 *
 * @property id The unique identifier of the product category.
 * @property name The name of the product category.
 * @property imageUrl The URL of the image representing the product category.
 * @property childrenProductCategories A list of child product categories.
 * @property parentCategoryId The unique identifier of the parent product category, if any.
 * @property productFilters A list of product filters associated with the category.
 */
@Serializable
data class NetworkProductCategory(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val childrenProductCategories: List<NetworkProductCategory>,
    val parentCategoryId: Int? = null,
    val productFilters: List<NetworkProductFilter>,
)

/**
 * Converts a [NetworkProductCategory] object to a [ProductCategory] domain model object.
 * This is used to map data from the network layer to the domain layer.
 */
fun NetworkProductCategory.asModel(): ProductCategory {
    return ProductCategory(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        childrenProductCategories = this.childrenProductCategories.map { it.asModel() },
        parentCategoryId = this.parentCategoryId,
        productFilters = this.productFilters.map { it.asModel() }
    )
}
