package com.example.deliveryapp.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.deliveryapp.BuildConfig
import com.example.deliveryapp.core.data.model.Diet
import com.example.deliveryapp.core.data.model.Product
import com.example.deliveryapp.core.data.model.ProductCategory
import com.example.deliveryapp.core.data.model.ProductFilter
import com.example.deliveryapp.core.data.model.ProductSortingCategory
import com.example.deliveryapp.core.data.paging.ProductPagingSource
import com.example.deliveryapp.core.network.NetworkDataSource
import com.example.deliveryapp.core.network.model.asModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository responsible for managing product-related data.
 *
 */
class ProductRepository @Inject constructor(
    private val network: NetworkDataSource
) {
    /**
     * Retrieves a paginated stream of products based on the provided category, dietary preferences,
     * sorting criteria, and additional filters.
     *
     * @param productCategoryId The unique identifier of the product category to filter by.
     * @param diets A list of [Diet] preferences to filter the products.
     * @param sortingCategory The [ProductSortingCategory] to apply to the products.
     * @param productFilters A list of [ProductFilter] to further filter the products.
     * @return A [Flow] emitting a [PagingData] of [Product] objects.
     */
    fun getProducts(
        productCategoryId: Int,
        diets: List<Diet>,
        sortingCategory: ProductSortingCategory,
        productFilters: List<ProductFilter>
    ): Flow<PagingData<Product>> {
        val pagingSourceFactory = {
            ProductPagingSource(network).apply {
                setProductCategoryId(productCategoryId)
                setDiets(diets)
                setSortingCategory(sortingCategory)
                setProductFilters(productFilters)
            }
        }
        return Pager(
            config = PagingConfig(pageSize = BuildConfig.MAX_PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        ).flow

    }

    /**
     * Fetches product categories from the network.
     *
     * @param productCategory The identifier for the specific category to retrieve. Defaults to 0.
     * @return A [ProductCategory] domain model containing the category data.
     */
    suspend fun getProductCategories(
        productCategory: Int = 0,
    ): ProductCategory {
        return network.getProductCategories(productCategory).asModel()
    }
}