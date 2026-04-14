package com.example.deliveryapp.core.designsystem.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.deliveryapp.R
import com.example.deliveryapp.core.designsystem.icon.Icons

/**
 * A center-aligned top app bar for the recipe list screen.
 *
 * @param navigateBack Callback to be invoked when the back navigation icon is clicked.
 * @param onShowFilterSheet Callback to be invoked when the filter action icon is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListTopAppBar(navigateBack: () -> Unit, onShowFilterSheet: () -> Unit) {

    val haptic = LocalHapticFeedback.current
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.recipe_ideas)
            )
        },
        navigationIcon = {
            NavigationButton { navigateBack() }
        },
        actions = {
            IconButton(onClick = {
                onShowFilterSheet()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)}
            ) {
                Icon(
                    painter = painterResource(id = Icons.filter),
                    contentDescription = stringResource(id = R.string.filter)
                )
            }
            IconButton(onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                TODO("Bookmarked Recipes screen")
                }
            ) {
                Icon(
                    painter = painterResource(id = Icons.bookmarks),
                    contentDescription = stringResource(id = R.string.bookmarked_recipes)
                )
            }

        }
    )
}

/**
 * A centered top app bar designed for the recipe detail screen.
 *
 * @param navigateBack A callback function invoked when the navigation icon is clicked
 * to return to the previous screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailTopAppBar(navigateBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.recipe_detail)
            )
        },
        navigationIcon = {
            NavigationButton { navigateBack() }
        },
    )
}

/**
 * A center-aligned top app bar for the product detail screen.
 *
 * @param navigateBack A callback to be invoked when the navigation back button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailTopAppBar(navigateBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.product_detail)
            )
        },
        navigationIcon = {
            NavigationButton { navigateBack() }
        },
    )
}

/**
 * A center-aligned top app bar for the checkout screen that provides navigation back
 * and an action to clear the shopping cart.
 *
 * @param navigateBack Callback to be invoked when the navigation icon is clicked.
 * @param clearShoppingCart Callback to be invoked when the clear cart action icon is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutTopAppBar(navigateBack: () -> Unit,
                      clearShoppingCart: () -> Unit,) {
    val haptic = LocalHapticFeedback.current
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.feature_checkout_title)
            )
        },
        navigationIcon = {
            NavigationButton { navigateBack() }
        },
        actions = {
            IconButton(onClick = {
                clearShoppingCart()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)}
            ) {
                Icon(
                    painter = painterResource(id = Icons.delete),
                    contentDescription = stringResource(id = R.string.clear_cart)
                )
            }

        }
    )
}

/**
 * A center-aligned top app bar displayed on the checkout screen when the cart is empty.
 *
 * @param navigateBack Callback to be invoked when the navigation icon is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmptyCheckoutTopAppBar(navigateBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.feature_checkout_title)
            )
        },
        navigationIcon = {
            NavigationButton { navigateBack() }
        },
    )
}

/**
 * A center-aligned top app bar for the product categories screen.
 *
 * @param navigateBack The callback invoked when the navigation icon is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCategoryTopAppBar(
    navigateBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.feature_product_categories_title)
            )
        },
        navigationIcon = {
            NavigationButton { navigateBack() }
        }
    )
}

/**
 * A center-aligned top app bar used in the products list screen.
 *
 * @param navigateBack Callback invoked when the back navigation button is clicked.
 * @param productCategoryName The name of the category to display as the title.
 * @param onShowFilterSheet Callback invoked when the filter action button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsTopAppBar(navigateBack: () -> Unit,
                      productCategoryName: String,
                      onShowFilterSheet: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = productCategoryName
            )
        },
        navigationIcon = {
            NavigationButton { navigateBack() }
        },
        actions = {
            IconButton(onClick = {
                onShowFilterSheet()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)}
            ) {
                Icon(
                    painter = painterResource(id = Icons.filter),
                    contentDescription = stringResource(id = R.string.filter)
                )
            }
            IconButton(onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                TODO("Favorite Products screen")
                }
            ) {
                Icon(
                    painter = painterResource(id = Icons.favorite),
                    contentDescription = stringResource(id = R.string.favorite_products)
                )
            }

        }
    )
}



