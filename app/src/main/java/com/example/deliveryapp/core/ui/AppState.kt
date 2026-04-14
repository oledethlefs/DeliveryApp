package com.example.deliveryapp.core.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.deliveryapp.feature.checkout.navigation.navigateToCheckout
import com.example.deliveryapp.feature.products.navigation.navigateToProducts
import com.example.deliveryapp.feature.recipes.navigation.navigateToRecipes
import com.example.deliveryapp.navigation.TopLevelDestination

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
): AppState {
    return remember(
        navController
    ) {
        AppState(
            navController = navController,
        )
    }
}

/**
 * Responsible for maintaining the state of the application UI, specifically handling the navigation.
 */
@Stable
class AppState(
    val navController: NavHostController,
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)
    val currentDestination: NavDestination?
        @Composable get() {
            // Collect the currentBackStackEntryFlow as a state
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            // Fallback to previousDestination if currentEntry is null
            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentTopLevelDestination: TopLevelDestination?
        @SuppressLint("RestrictedApi") @Composable get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                currentDestination?.hasRoute(route = topLevelDestination.route) == true
            }
        }

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {

            val topLevelNavOptions = navOptions {
                launchSingleTop = true
                restoreState = true
            }

            when (topLevelDestination) {
                TopLevelDestination.RECIPES -> navController.navigateToRecipes(topLevelNavOptions)
                TopLevelDestination.PRODUCTS -> navController.navigateToProducts(topLevelNavOptions)
                TopLevelDestination.PROFILE -> navController.navigateToProducts(topLevelNavOptions)
                TopLevelDestination.CHECKOUT -> navController.navigateToCheckout(topLevelNavOptions)
            }

    }

}