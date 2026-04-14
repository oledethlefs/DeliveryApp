package com.example.deliveryapp.core.data.repository

import com.example.deliveryapp.core.datastore.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.example.deliveryapp.core.data.model.UserData
import com.example.deliveryapp.core.data.model.Diet


/**
 * Repository that provides offline-first access to user data.
 *
 * This repository delegates all operations to the [UserPreferencesDataSource], ensuring that data
 * is persisted locally and can be accessed even when the network is unavailable.
 */
internal class OfflineFirstUserDataRepository @Inject constructor(
    private val userPreferencesDataSource : UserPreferencesDataSource,
) : UserDataRepository {

    /**
     * Provides a [Flow] of [UserData] representing the current user's data.
     *
     * This property exposes a stream of user data obtained from the underlying [userPreferencesDataSource].
     */
    override val userData: Flow<UserData> = userPreferencesDataSource.userData

    /**
     * A [Flow] of the user's selected dietary preferences.
     *
     * This property maps the boolean flags within the [userData] stream into a list of [Diet] enums.
     */
    override val userDiets: Flow<List<Diet>> = userPreferencesDataSource.userData.map { userData ->
        buildList {
            if (userData.isVegan) add(Diet.VEGAN)
            if (userData.isVegetarian) add(Diet.VEGETARIAN)
            if (userData.isGlutenFree) add(Diet.GLUTEN_FREE)
            if (userData.isDairyFree) add(Diet.DAIRY_FREE)
            if (userData.isLowCarb) add(Diet.LOW_CARB)
            if (userData.isOrganic) add(Diet.ORGANIC)
            if (userData.isHalal) add(Diet.HALAL)
            if (userData.isPescetarian) add(Diet.PESCETARIAN)
            if (userData.isGmoFree) add(Diet.GMO_FREE)
        }
    }

    /**
     * Updates the user's dietary preferences.
     *
     * This function persists the provided list of [Diet] preferences to the underlying
     * [userPreferencesDataSource], ensuring the user's dietary profile is updated offline-first.
     *
     * @param diets The list of [Diet] categories to be associated with the user.
     */
    override suspend fun updateDiets(diets: List<Diet>) {
        userPreferencesDataSource.updateDiets(diets)
    }



    /**
     * Sets the user ID in the data store.
     *
     * This function persists the given [userId] to the user preferences data store
     * using the [userPreferencesDataSource].
     *
     * @param userId The ID of the user to be stored.
     */
    override suspend fun setUserId(userId: Int) {
        userPreferencesDataSource.setUserId(userId)
    }

    /**
     * Updates the bookmark status for a specific recipe.
     *
     * This function persists the bookmarked state of the recipe identified by [recipeId]
     * to the local data store via the [userPreferencesDataSource].
     *
     * @param recipeId The unique identifier of the recipe to be bookmarked.
     */
    override suspend fun setRecipeBookmarked(recipeId: Int) {
        userPreferencesDataSource.setRecipeBookmarked(recipeId)
    }

    /**
     * Toggles the favorite status of a product.
     *
     * This function persists the favorite status of the product identified by [productId]
     * to the user preferences data store using the [userPreferencesDataSource].
     *
     * @param productId The ID of the product to be marked as a favorite.
     */
    override suspend fun setProductFavorite(productId: Int) {
        userPreferencesDataSource.setProductFavorite(productId)
    }



}