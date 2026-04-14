package com.example.deliveryapp.core.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.deliveryapp.core.data.model.Diet
import com.example.deliveryapp.core.data.model.Product
import com.example.deliveryapp.core.data.model.ProductFilter
import com.example.deliveryapp.core.data.model.ProductSortingCategory
import com.example.deliveryapp.core.network.NetworkDataSource
import com.example.deliveryapp.core.network.model.asModel
import okio.IOException
import retrofit2.HttpException

/**
 * A [PagingSource] that fetches [Product] items from the [NetworkDataSource].
 *
 * This implementation handles incremental loading of products based on specific criteria
 * such as category, sorting preferences, dietary restrictions, and additional filters.
 * It uses the product ID as the key for pagination to fetch subsequent pages.
 *
 * @property network The remote data source used to retrieve product data.
 */
class ProductPagingSource (
    private val network: NetworkDataSource
) : PagingSource<Int, Product>() {

    private var productCategoryId: Int = 0
    private var sortingCategory: ProductSortingCategory = ProductSortingCategory.POPULARITY
    private var diets: List<Diet> = emptyList()
    private var productFilters: List<ProductFilter> = emptyList()





    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val lastProductId = params.key
            val diets = diets.map {it.apiKey}
            val filters = productFilters.map {it.id}
            val products = try {
                network.getProducts(
                    productCategoryId = productCategoryId,
                    diets = diets,
                    afterId = lastProductId,
                    sortBy = sortingCategory.apiKey,
                    filters = filters
                )
            } catch (e: Exception) {
                Log.e("ProductPagingSource", "Error fetching products with category ID: $productCategoryId, last product ID: $lastProductId, diets: $diets, filters: $filters, sorting category: ${sortingCategory.apiKey}", e)
                emptyList()
            }
            LoadResult.Page(
                data = products.map { it.asModel() },
                prevKey = if (lastProductId == null) null else products.firstOrNull()?.id,
                nextKey = if (products.isEmpty()) null else products.last().id
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { state.closestItemToPosition(it)?.id }
    }

    fun setProductCategoryId(categoryId: Int) {
        productCategoryId = categoryId
    }

    fun setSortingCategory(category: ProductSortingCategory) {
        sortingCategory = category
    }

    fun setDiets(dietList: List<Diet>) {
        diets = dietList
    }

    fun setProductFilters(filters: List<ProductFilter>) {
        productFilters = filters
    }
}