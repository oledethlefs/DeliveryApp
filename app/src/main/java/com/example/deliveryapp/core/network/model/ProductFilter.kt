package com.example.deliveryapp.core.network.model

import com.example.deliveryapp.core.data.model.ProductFilter
import kotlinx.serialization.Serializable

/**
 * Network representation of a product filter.
 *
 * @property id The unique identifier of the filter.
 * @property name The display name of the filter.
 * @property isBrandFilter Boolean flag indicating whether this filter specifically
 * refers to a brand category.
 */
@Serializable
data class NetworkProductFilter(
    val id: Int,
    val name: String,
    val isBrandFilter: Boolean,
)

/**
 * Converts the [NetworkProductFilter] data transfer object into a domain-level [ProductFilter] model.
 *
 * @return A [ProductFilter] instance containing the equivalent data from the network response.
 */
fun NetworkProductFilter.asModel(): ProductFilter {
    return ProductFilter(
        id = this.id,
        name = this.name,
        isBrandFilter = this.isBrandFilter
    )
}