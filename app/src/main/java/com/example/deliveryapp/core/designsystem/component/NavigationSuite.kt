package com.example.deliveryapp.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.deliveryapp.navigation.TopLevelDestination

/**
 * A scaffold that provides adaptive navigation UI based on the current window size class.
 *
 * This component automatically switches between a bottom navigation bar, a navigation rail,
 * or a navigation drawer to provide an optimal navigation experience across different device
 * factors (mobile, tablet, desktop).
 *
 * @param currentTopLevelDestination The currently selected [TopLevelDestination].
 * @param navigateToDestination A callback invoked when a new [TopLevelDestination] is selected.
 * @param modifier The [Modifier] to be applied to the scaffold.
 * @param content The composable content to be displayed within the scaffold's main area.
 */
@Composable
fun NavigationSuiteScaffold(
    currentTopLevelDestination: TopLevelDestination?,
    navigateToDestination: (TopLevelDestination) -> Unit,
    cartItemCount: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {

    val navigationSuiteItemColors = NavigationSuiteItemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
            indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
            indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
        )
    )
    val navigationSuiteColors = NavigationSuiteDefaults.colors(
        navigationBarContainerColor = MaterialTheme.colorScheme.surface,
        navigationRailContainerColor = MaterialTheme.colorScheme.surface
    )


    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TopLevelDestination.entries.forEach {
                item(
                    selected = it == currentTopLevelDestination,
                    onClick = { navigateToDestination(it) },
                    icon = {
                        if (it == TopLevelDestination.CHECKOUT && it != currentTopLevelDestination) {
                            CheckoutBadgedBox(
                                cartItemCount = cartItemCount
                            )
                        } else {
                            Icon(
                                painter = if (it == currentTopLevelDestination) painterResource(it.selectedIcon) else painterResource(it.unselectedIcon),
                                contentDescription = stringResource(it.contentDescription)
                            )
                        }


                    },
                    label = { Text(stringResource(it.label)) },
                    colors = navigationSuiteItemColors

                )


            }
        },
        navigationSuiteColors = navigationSuiteColors,
        modifier = modifier,
    ) {
        Column(
            Modifier
                .safeDrawingPadding()
                .fillMaxSize()
        ) {
            content()
        }


    }
}
