package com.example.deliveryapp.core.network.retrofit

import com.example.deliveryapp.BuildConfig
import com.example.deliveryapp.core.network.NetworkDataSource
import com.example.deliveryapp.core.network.model.NetworkProduct
import com.example.deliveryapp.core.network.model.NetworkProductCategory
import com.example.deliveryapp.core.network.model.NetworkRecipe
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType


/**
 * Retrofit API declaration for delivery app network operations.
 * Defines the endpoints for fetching recipes, product categories, products, and calculating service fees.
 */
private interface RetrofitNetworkApi {
    @GET(value = "recipes")
    suspend fun getRecipes(
        @Query("recipe_category") recipeCategory: String,
        @Query("diet") diets: List<String>?,
        @Query("after") afterId: Int?,
        @Query("sort_by") sortBy: String,

        ): NetworkResponse<List<NetworkRecipe>>

    @GET(value = "recipes")
    suspend fun getRecipes(
        @Query("id") ids: List<Int>,
    ): NetworkResponse<List<NetworkRecipe>>

    @GET(value = "product_categories")
    suspend fun getProductCategories(
        @Query("product_category_id") productCategoryId: Int?,
    ): NetworkResponse<NetworkProductCategory>

    @GET(value = "products")
    suspend fun getProducts(
        @Query("product_category_id") productCategoryId: Int,
        @Query("diet") diets: List<String>?,
        @Query("afterId") afterId: Int?,
        @Query("sort_by") sortBy: String?,
        @Query("filter") filters: List<Int>?,
    ): NetworkResponse<List<NetworkProduct>>

    @GET(value = "products_from_recipe")
    suspend fun getProductsFromRecipe(
        @Query("recipe_id") recipeId: Int,
    ): NetworkResponse<List<NetworkProduct>>

    @GET(value = "product")
    suspend fun getProducts(
        @Query("product_id") productIds: List<Int>,
    ): NetworkResponse<List<NetworkProduct>>

    @GET(value = "service_fee")
    suspend fun getServiceFee(
        @Query("product_id") productIds: List<Int>,
    ): NetworkResponse<Int>



}

private const val BASE_URL = BuildConfig.BASE_URL


@Serializable
private data class NetworkResponse<T>(
    val data: T,
)


/**
 * Retrofit backed [NetworkDataSource] implementation that facilitates network requests
 * to the production backend for retrieving recipes, products, categories, and fee information.
 *
 * @property networkJson The [Json] serializer used for parsing network responses.
 */
class ProdNetworkDataSource @Inject constructor(
    networkJson: Json,
) : NetworkDataSource {


    private val networkApi =
        Retrofit.Builder()
            .baseUrl("http://$BASE_URL")
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(RetrofitNetworkApi::class.java)

    override suspend fun getRecipes(
        recipeCategory: String,
        diets: List<String>?,
        afterId: Int?,
        sortBy: String
    ): List<NetworkRecipe> {
        return networkApi.getRecipes(recipeCategory, diets, afterId, sortBy).data
    }

    override suspend fun getRecipes(ids: List<Int>): List<NetworkRecipe> {
        return networkApi.getRecipes(ids).data
    }

    override suspend fun getProductCategories(productCategoryId: Int?): NetworkProductCategory {
        return networkApi.getProductCategories(productCategoryId).data
    }

    override suspend fun getProducts(
        productCategoryId: Int,
        diets: List<String>?,
        afterId: Int?,
        sortBy: String?,
        filters: List<Int>?
    ): List<NetworkProduct> {
        return networkApi.getProducts(productCategoryId, diets, afterId, sortBy, filters).data

    }

    override suspend fun getProductsFromRecipe(recipeId: Int): List<NetworkProduct> {
        return networkApi.getProductsFromRecipe(recipeId).data

    }

    override suspend fun getProducts(productIds: List<Int>): List<NetworkProduct> {
        if (productIds.isEmpty()) return emptyList()
        return networkApi.getProducts(productIds).data
    }

    override suspend fun getServiceFee(productIds: List<Int>) : Int {
        return networkApi.getServiceFee(productIds).data
    }


}