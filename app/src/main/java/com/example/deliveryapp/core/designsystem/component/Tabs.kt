package com.example.deliveryapp.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4A
import androidx.compose.ui.tooling.preview.Devices.PIXEL_TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deliveryapp.core.data.model.RecipeCategory
import com.example.deliveryapp.core.designsystem.theme.DeliveryAppTheme
import com.example.deliveryapp.navigation.TopLevelDestination

///**
// * A composable that displays a scrollable set of tabs corresponding to [RecipeCategory] entries,
// * synchronized with a [HorizontalPager].
// *
// * @param selectedRecipeCategory The currently active recipe category.
// * @param onTabSelected Callback invoked when a new tab is selected or swiped to.
// * @param modifier The [Modifier] to be applied to the layout.
// * @param content The composable lambda that defines the content for each page of the pager.
// * It provides the [RecipeCategory] corresponding to the current page and the [PagerState].
// */
//@Composable
//fun RecipeTabs(selectedRecipeCategory: RecipeCategory,
//               onTabSelected: (RecipeCategory) -> Unit,
//               modifier: Modifier = Modifier,
//               content: @Composable (recipeCategoryForPage: RecipeCategory) -> Unit){
//    val haptic = LocalHapticFeedback.current
//    val pagerState = rememberPagerState {
//        RecipeCategory.entries.size
//    }
//    // Scroll to the selected tab when selectedRecipeCategory changes externally
//    LaunchedEffect(selectedRecipeCategory.ordinal) {
//        if (selectedRecipeCategory.ordinal != pagerState.currentPage) pagerState.animateScrollToPage(selectedRecipeCategory.ordinal)
//    }
//    // Update the selected tab when the user swipes the pager
//    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
//        if (!pagerState.isScrollInProgress) {
//            onTabSelected(RecipeCategory.entries[pagerState.currentPage])
//        }
//    }
//
//    Column (modifier) {
//        TabRow(selectedTabIndex = selectedRecipeCategory.ordinal,
//            containerColor = Color.Transparent) {
//            RecipeCategory.entries.forEachIndexed {
//                    index, recipeCategory ->
//                Tab(
//                    selected = index == selectedRecipeCategory.ordinal,
//                    onClick = {  onTabSelected(recipeCategory); haptic.performHapticFeedback(HapticFeedbackType.LongPress) },
//                    text = { Text(text = stringResource(id = recipeCategory.label)) },
//                    selectedContentColor = MaterialTheme.colorScheme.primary,
//                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
//                    icon = {
//                        Icon(
//                            painter = if (index == selectedRecipeCategory.ordinal) painterResource(id = recipeCategory.icon_selected) else painterResource(id = recipeCategory.icon_unselected),
//                            contentDescription = stringResource(id =recipeCategory.label)
//                        )
//
//                    }
//                )
//
//            }
//        }
//        HorizontalPager(state = pagerState,
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f)
//        )  {pageIndex ->
//            Column (Modifier
//                .fillMaxSize(),
//                verticalArrangement = Arrangement.spacedBy(16.dp)) {
//                content(RecipeCategory.entries[pageIndex])
//            }
//
//        }
//    }
//
//}

/**
 * A composable that displays a scrollable set of tabs corresponding to [RecipeCategory] entries,
 * synchronized with a [HorizontalPager].
 *
 * @param selectedRecipeCategory The currently active recipe category.
 * @param onTabSelected Callback invoked when a new tab is selected or swiped to.
 * @param modifier The [Modifier] to be applied to the layout.
 * @param content The composable lambda that defines the content for each page of the pager.
 * It provides the [RecipeCategory] corresponding to the current page and the [PagerState].
 */
@Composable
fun RecipeTabs(
    selectedRecipeCategory: RecipeCategory,
    onTabSelected: (RecipeCategory) -> Unit,    modifier: Modifier = Modifier,
    content: @Composable (recipeCategoryForPage: RecipeCategory) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val pagerState = rememberPagerState {
        RecipeCategory.entries.size
    }

    // 1. Sync Pager with external state (e.g., initial load or deep link)
    LaunchedEffect(selectedRecipeCategory) {
        if (selectedRecipeCategory.ordinal != pagerState.currentPage) {
            pagerState.animateScrollToPage(selectedRecipeCategory.ordinal)
        }
    }

    // 2. Sync external state with Pager (only when settled to avoid loop lag)
    LaunchedEffect(pagerState.currentPage) {
        onTabSelected(RecipeCategory.entries[pagerState.currentPage])
    }

    Column(modifier) {
        // CHANGE: Use pagerState.currentPage for instant UI feedback
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent
        ) {
            RecipeCategory.entries.forEachIndexed { index, recipeCategory ->
                val isSelected = index == pagerState.currentPage

                Tab(
                    selected = isSelected,
                    onClick = {
                        // When clicking, we update state and the pager
                        onTabSelected(recipeCategory)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                    text = { Text(text = stringResource(id = recipeCategory.label)) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    icon = {
                        Icon(
                            painter = painterResource(
                                id = if (isSelected) recipeCategory.icon_selected
                                else recipeCategory.icon_unselected
                            ),
                            contentDescription = stringResource(id = recipeCategory.label)
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { pageIndex ->
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content(RecipeCategory.entries[pageIndex])
            }
        }
    }
}



@Preview (device = PIXEL_4A)
@Composable
fun RecipeTabsPhonePreview() {
    DeliveryAppTheme {
        var currentTopLevelDestination by remember { mutableStateOf(TopLevelDestination.RECIPES) }
        NavigationSuiteScaffold(
            currentTopLevelDestination = currentTopLevelDestination,
            navigateToDestination = { currentTopLevelDestination = it },
            modifier = Modifier,
            cartItemCount = 0,
        ) {
            var currentSelectedRecipeCategory by remember { mutableStateOf(RecipeCategory.DINNER) }
            RecipeTabs(
                selectedRecipeCategory = currentSelectedRecipeCategory,
                onTabSelected = { currentSelectedRecipeCategory = it },
                modifier = Modifier,
                content = { _->}
            )
        }
    }
}

@Preview (device = PIXEL_TABLET)
@Composable
fun RecipeTabsTabletPreview() {
    DeliveryAppTheme {
        var currentTopLevelDestination by remember { mutableStateOf(TopLevelDestination.RECIPES) }
        NavigationSuiteScaffold(
            currentTopLevelDestination = currentTopLevelDestination,
            navigateToDestination = { currentTopLevelDestination = it },
            modifier = Modifier,
            cartItemCount = 0,
        ) {
            var currentSelectedRecipeCategory by remember { mutableStateOf(RecipeCategory.DINNER) }
            RecipeTabs(
                selectedRecipeCategory = currentSelectedRecipeCategory,
                onTabSelected = { currentSelectedRecipeCategory = it },
                modifier = Modifier,
                content = {_->}
            )
        }
    }
}