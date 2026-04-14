package com.example.deliveryapp.feature.checkout.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.deliveryapp.feature.checkout.CheckoutRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.Serializable

/**
 * Navigation destination for the checkout screen.
 * Used to define the route within the navigation graph for the order finalization process.
 */
@Serializable object CheckoutRoute

/**
 * Navigates to the checkout screen.
 *
 * @param navOptions Optional [NavOptions] for the navigation request.
 */
fun NavController.navigateToCheckout(navOptions: NavOptions? = null) =
    navigate(route = CheckoutRoute, navOptions)
/**
 * Adds the checkout screen destination to the [NavGraphBuilder].
 *
 * This function defines the composable for the [CheckoutRoute], providing the necessary
 * navigation callbacks to the [CheckoutRoute] UI component.
 *
 * @param navigateBack Callback to return to the previous screen in the navigation stack.
 * @param navigateToProducts Callback to navigate to the product listing screen, typically after a successful checkout.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun NavGraphBuilder.checkOutScreen(
    navigateBack: () -> Unit,
    navigateToProducts: () -> Unit,
) {
    composable<CheckoutRoute> {
        CheckoutRoute(
            navigateBack = navigateBack,
            navigateToProducts = navigateToProducts
        )
    }
}
