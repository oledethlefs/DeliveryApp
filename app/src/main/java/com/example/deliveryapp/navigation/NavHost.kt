package com.example.deliveryapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.deliveryapp.core.ui.AppState
import com.example.deliveryapp.feature.checkout.navigation.CheckoutRoute
import com.example.deliveryapp.feature.checkout.navigation.checkOutScreen
import com.example.deliveryapp.feature.products.navigation.navigateToProducts
import com.example.deliveryapp.feature.products.navigation.productsScreen
import com.example.deliveryapp.feature.recipes.navigation.recipesScreen

/**
 * Top-level navigation host for the application that defines the navigation graph.
 *
 * This composable connects the [AppState] to the [NavHost], setting up the available
 * routes including recipes, products, and the checkout flow.
 *
 * @param appState The global state of the application.
 * @param modifier Modifier to be applied to the layout.
 */
@Composable
fun NavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = CheckoutRoute,
        modifier = modifier,
    ) {
        recipesScreen(
            navigateBack = navController::navigateUp
        )
        productsScreen(
            navigateBack = navController::navigateUp
        )
        checkOutScreen(
            navigateBack = navController::navigateUp,
            navigateToProducts = navController::navigateToProducts

        )
    }
}