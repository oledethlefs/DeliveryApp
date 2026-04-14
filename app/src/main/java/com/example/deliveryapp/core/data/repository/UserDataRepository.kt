package com.example.deliveryapp.core.data.repository

import com.example.deliveryapp.core.data.model.Diet
import com.example.deliveryapp.core.data.model.UserData
import kotlinx.coroutines.flow.Flow

/**
 * Data layer interface for managing user-specific preferences and metadata.
 *
 * This repository provides access to reactive streams of [UserData] and [Diet] information,
 * and exposes methods to modify user settings, authentication state, and item-specific
 * interactions like bookmarks and favorites.
 */
interface UserDataRepository {

    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    val userDiets : Flow<List<Diet>>

    suspend fun updateDiets(diets: List<Diet>)


    suspend fun setUserId(userId: Int)

    suspend fun setRecipeBookmarked(recipeId: Int)

    suspend fun setProductFavorite(productId: Int)

}