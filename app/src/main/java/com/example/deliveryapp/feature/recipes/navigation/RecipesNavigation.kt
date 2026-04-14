package com.example.deliveryapp.feature.recipes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.deliveryapp.feature.recipes.RecipesRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.Serializable

/**
 * Navigation destination for the recipe screen.
 */
@Serializable object RecipesRoute

/**
 * Navigates to the recipe screen.
 *
 * @param navOptions Optional [NavOptions] for the navigation request.
 */
fun NavController.navigateToRecipes(navOptions: NavOptions? = null) =
    navigate(route = RecipesRoute, navOptions)

/**
 * Adds the recipe screen destination to the [NavGraphBuilder].
 *
 * This function defines the composable for the [RecipesRoute], providing the necessary
 * navigation callbacks to the [RecipesRoute] UI component.
 *
 * @param navigateBack Callback to return to the previous screen in the navigation stack.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun NavGraphBuilder.recipesScreen(
    navigateBack: () -> Unit,
) {
    composable<RecipesRoute> {
        RecipesRoute(
            navigateBack = navigateBack
        )
    }
}
