package com.example.deliveryapp.core.network

import com.example.deliveryapp.core.network.model.NetworkProduct
import com.example.deliveryapp.core.network.model.NetworkProductCategory
import com.example.deliveryapp.core.network.model.NetworkRecipe

/**
 * Interface defining the network operations for the delivery application.
 * Acts as the primary entry point for fetching remote data related to recipes, products, and categories.
 */
interface NetworkDataSource {

    suspend fun getRecipes(recipeCategory: String, diets: List<String>? = null, afterId: Int? = null, sortBy: String): List<NetworkRecipe>

    suspend fun getRecipes(ids: List<Int>): List<NetworkRecipe>

    suspend fun getProductCategories(productCategoryId: Int? = null): NetworkProductCategory

    suspend fun getProducts(productCategoryId: Int, diets: List<String>? = null, afterId: Int? = null, sortBy: String? = null, filters: List<Int>? = null): List<NetworkProduct>

    suspend fun getProductsFromRecipe(recipeId: Int): List<NetworkProduct>

    suspend fun getProducts(productIds: List<Int>): List<NetworkProduct>

    suspend fun getServiceFee(productIds: List<Int>): Int


}