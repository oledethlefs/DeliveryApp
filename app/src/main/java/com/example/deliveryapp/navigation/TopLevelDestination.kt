package com.example.deliveryapp.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.deliveryapp.R
import com.example.deliveryapp.core.designsystem.icon.Icons
import com.example.deliveryapp.feature.checkout.navigation.CheckoutRoute
import com.example.deliveryapp.feature.products.navigation.ProductsRoute
import com.example.deliveryapp.feature.recipes.navigation.RecipesRoute
import kotlin.reflect.KClass

/**
 * Enumeration of top-level destinations in the application.
 */
enum class TopLevelDestination(
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    @StringRes val label: Int,
    @StringRes val contentDescription: Int,
    val route: KClass<*>,
) {
    RECIPES(
        selectedIcon = Icons.selected_recipes,
        unselectedIcon = Icons.recipes,
        label = R.string.feature_recipes_title,
        contentDescription = R.string.feature_recipes_title,
        route = RecipesRoute::class,
    ),
    PRODUCTS(
        selectedIcon = Icons.selected_products,
        unselectedIcon = Icons.products,
        label = R.string.feature_products_title,
        contentDescription = R.string.feature_products_title,
        route = ProductsRoute::class ,
    ),
    CHECKOUT(
        selectedIcon = Icons.selected_shopping_cart,
        unselectedIcon = Icons.shopping_cart,
        label = R.string.feature_checkout_title,
        contentDescription = R.string.feature_checkout_title,
        route = CheckoutRoute::class,
    ),
    PROFILE(
        selectedIcon = Icons.selected_profile,
        unselectedIcon = Icons.profile,
        label = R.string.feature_profile_title,
        contentDescription = R.string.feature_profile_title,
        route =RecipesRoute::class,
    ),
}