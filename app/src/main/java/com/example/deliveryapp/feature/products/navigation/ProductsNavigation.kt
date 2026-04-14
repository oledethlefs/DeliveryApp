package com.example.deliveryapp.feature.products.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.deliveryapp.feature.products.ProductsRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.Serializable

/**
 * Navigation destination for the product screen.
 */
@Serializable object ProductsRoute

/**
 * Navigates to the product screen.
 *
 * @param navOptions Optional [NavOptions] for the navigation request.
 */
fun NavController.navigateToProducts(navOptions: NavOptions? = null) =
    navigate(route = ProductsRoute, navOptions)

/**
 * Adds the product screen destination to the [NavGraphBuilder].
 *
 * This function defines the composable for the [ProductsRoute], providing the necessary
 * navigation callbacks to the [ProductsRoute] UI component.
 *
 * @param navigateBack Callback to return to the previous screen in the navigation stack.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun NavGraphBuilder.productsScreen(
    navigateBack: () -> Unit,
) {
    composable<ProductsRoute> {
        ProductsRoute(
            navigateBack = navigateBack
        )
    }
}
