package com.example.deliveryapp.core.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.deliveryapp.core.designsystem.component.NavigationSuiteScaffold
import com.example.deliveryapp.core.designsystem.theme.DeliveryAppTheme
import com.example.deliveryapp.feature.checkout.CheckoutViewModel
import com.example.deliveryapp.navigation.NavHost

@Composable
fun App(
    appState: AppState = rememberAppState(),
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
) {
    DeliveryAppTheme {
        NavigationSuiteScaffold(
            currentTopLevelDestination = appState.currentTopLevelDestination,
            navigateToDestination = appState::navigateToTopLevelDestination,
            cartItemCount = checkoutViewModel.totalCartItemsCount.collectAsStateWithLifecycle().value
        ) {
            NavHost(
                appState = appState,
            )

        }
    }
}


