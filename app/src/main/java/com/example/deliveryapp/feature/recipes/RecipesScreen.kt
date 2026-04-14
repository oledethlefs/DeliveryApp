package com.example.deliveryapp.feature.recipes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.deliveryapp.R
import com.example.deliveryapp.core.data.model.Diet
import com.example.deliveryapp.core.data.model.Recipe
import com.example.deliveryapp.core.data.model.RecipeCategory
import com.example.deliveryapp.core.data.model.RecipeSortingCategory
import com.example.deliveryapp.core.data.model.spaghettiRecipe
import com.example.deliveryapp.core.data.model.steakRecipe
import com.example.deliveryapp.core.designsystem.component.AddToShoppingCartFAB
import com.example.deliveryapp.core.designsystem.component.FilterAndSortSheet
import com.example.deliveryapp.core.designsystem.component.RecipeCard
import com.example.deliveryapp.core.designsystem.component.RecipeDetail
import com.example.deliveryapp.core.designsystem.component.RecipeDetailTopAppBar
import com.example.deliveryapp.core.designsystem.component.RecipeItemShimmer
import com.example.deliveryapp.core.designsystem.component.RecipeListTopAppBar
import com.example.deliveryapp.core.designsystem.component.RecipeTabs
import com.example.deliveryapp.core.designsystem.component.ScrollToTopButtonFAB
import com.example.deliveryapp.core.designsystem.component.StandardSearchBar
import com.example.deliveryapp.core.designsystem.theme.DeliveryAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

/**
 * Entry point for the recipes screen, responsible for initializing the [RecipesViewModel].
 *
 *
 * @param modifier Modifier to be applied to the recipe screen layout.
 * @param viewModel The [RecipesViewModel] that provides data and handles business logic.
 * @param navigateBack Callback to be invoked when the user navigates to the previous destination.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun RecipesRoute(
    modifier: Modifier = Modifier,
    viewModel: RecipesViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    RecipesScreen(
        modifier = modifier,
        bookmarkedRecipeIds = viewModel.bookmarkedRecipeIds.collectAsStateWithLifecycle().value,
        selectedDiets = viewModel.selectedDiets.collectAsStateWithLifecycle().value,
        onDietSelected = viewModel::updateSelectedDiets,
        selectedRecipeCategory = viewModel.selectedRecipeCategory.collectAsStateWithLifecycle().value,
        selectedRecipeSortingCategory = viewModel.selectedRecipeSortingCategory.collectAsStateWithLifecycle().value,
        setRecipeCategory = viewModel::setRecipeCategory,
        setSortingCategory = viewModel::setSortingCategory,
        onBookmarkClick = viewModel::bookmarkRecipe,
        navigateBack = navigateBack,
        addRecipeToShoppingCart = viewModel::addRecipeToShoppingCart,
        deleteRecipeFromShoppingCart = viewModel::deleteRecipeFromShoppingCart,
        getRecipesForCategory = viewModel::getRecipesForCategory,
    )
}

/**
 * The main UI component for the recipes screen that handles the adaptive layout for displaying
 * the recipes lists, and recipe details.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param bookmarkedRecipeIds List of IDs for recipes marked as bookmarked by the user.
 * @param selectedDiets List of currently active diet filters.
 * @param onDietSelected Callback to update the list of selected diet filters.
 * @param selectedRecipeCategory The current category used for displaying recipes.
 * @param selectedRecipeSortingCategory The current criteria used for sorting recipes.
 * @param setRecipeCategory Callback to update the current category selection.
 * @param onBookmarkClick Callback to toggle the bookmark status of a [Recipe].
 * @param setSortingCategory Callback to update the recipe sorting criteria.
 * @param navigateBack Callback to navigate away from the recipes feature.
 * @param addRecipeToShoppingCart Callback to add a specific [Recipe] to the shopping cart.
 * @param getRecipesForCategory Callback to fetch recipes for a specific category.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun RecipesScreen(
    modifier: Modifier = Modifier,
    bookmarkedRecipeIds: List<Int>,
    selectedDiets: List<Diet>,
    onDietSelected: (List<Diet>) -> Unit,
    selectedRecipeCategory: RecipeCategory,
    selectedRecipeSortingCategory: RecipeSortingCategory,
    setRecipeCategory: (RecipeCategory) -> Unit,
    onBookmarkClick: (Recipe) -> Unit,
    setSortingCategory: (RecipeSortingCategory) -> Unit,
    navigateBack: () -> Unit,
    addRecipeToShoppingCart: (Recipe) -> Unit,
    deleteRecipeFromShoppingCart: (Recipe) -> Unit,
    getRecipesForCategory: (RecipeCategory) -> Flow<PagingData<Recipe>>,

    ) {

    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<Recipe>()
    val scope = rememberCoroutineScope()

    val pagingItemsMap = RecipeCategory.entries.associateWith { category ->
        getRecipesForCategory(category).collectAsLazyPagingItems()

    }

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                RecipesListPane(
                    scope = scope,
                    selectedDiets = selectedDiets,
                    onDietSelected = onDietSelected,
                    selectedRecipeSortingCategory = selectedRecipeSortingCategory,
                    setSortingCategory = setSortingCategory,
                    onRecipeClick = { recipe ->
                        // Navigate to the detail pane with the passed recipe
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                recipe
                            )
                        }
                    },
                    onBookmarkClick = { recipe ->
                        onBookmarkClick(recipe)
                    },
                    selectedRecipeCategory = selectedRecipeCategory,
                    setRecipeCategory = setRecipeCategory,
                    navigateBack = navigateBack,
                    bookmarkedRecipeIds = bookmarkedRecipeIds,
                    pagingItemsMap = pagingItemsMap
                )
            }
        },
        detailPane = {
            AnimatedPane {
                // Show the detail pane content if selected recipe is available
                scaffoldNavigator.currentDestination?.contentKey?.let {
                    RecipeDetailPane(
                        scope = scope,
                        recipe = it,
                        isBookmarked = it.id in bookmarkedRecipeIds,
                        onRecipeClick = {},
                        onBookmarkClick = { recipe ->
                            onBookmarkClick(recipe)
                        },
                        navigateBack = {
                            scope.launch {
                                scaffoldNavigator.navigateBack()
                            }
                        },
                        addRecipeToShoppingCart = addRecipeToShoppingCart,
                        deleteRecipeFromShoppingCart = deleteRecipeFromShoppingCart

                    )
                }
            }
        },
    )
}


/**
 * Displays the detailed information for a specific recipe using [RecipeDetail]
 *
 * @param modifier The [Modifier] to be applied to the pane.
 * @param recipe The [Recipe] object to display.
 * @param scope The [CoroutineScope] used for launching snack bars and asynchronous actions.
 * @param isBookmarked A boolean indicating whether the current recipe is in the user's bookmarks.
 * @param onRecipeClick Callback invoked when the recipe is clicked (optional).
 * @param onBookmarkClick Callback invoked when the bookmark icon is toggled.
 * @param navigateBack Callback invoked to return to the previous screen or pane.
 * @param addRecipeToShoppingCart Callback invoked when the user adds the recipe to their cart.
 */
@Composable
internal fun RecipeDetailPane(
    modifier: Modifier = Modifier,
    recipe: Recipe,
    scope: CoroutineScope,
    isBookmarked: Boolean,
    onRecipeClick: (Recipe) -> Unit = {},
    onBookmarkClick: (Recipe) -> Unit,
    navigateBack: () -> Unit,
    addRecipeToShoppingCart: (Recipe) -> Unit,
    deleteRecipeFromShoppingCart: (Recipe) -> Unit,

    ) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    Scaffold(
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)},
        topBar = {
            RecipeDetailTopAppBar(navigateBack)
        },
        floatingActionButton = {
            AddToShoppingCartFAB(
                onClick = {
                    scope.launch {
                        addRecipeToShoppingCart(recipe)
                        val result = snackbarHostState.showSnackbar(context.getString(R.string.recipe_added_to_cart), actionLabel = context.getString(R.string.undone), duration = SnackbarDuration.Short)
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                scope.launch {
                                    deleteRecipeFromShoppingCart(recipe)
                                }
                            }
                            SnackbarResult.Dismissed -> {}
                        }
                    }
                     }
            )
        }
    ) { innerPadding ->
        RecipeDetail(
            recipe = recipe,
            isBookmarked = isBookmarked,
            onRecipeClick = onRecipeClick,
            onBookmarkClick = {
                scope.launch {
                    onBookmarkClick(it)
                }

            },
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                )
        )
    }

}

/**
 * A composable pane that displays a list of [Recipe]s within a specific [RecipeCategory].
 *
 * @param scope The [CoroutineScope] used for launching snack bars and scroll animations.
 * @param modifier The [Modifier] to be applied to the pane.
 * @param bookmarkedRecipeIds The list of recipe IDs that are bookmarked by the user.
 * @param onRecipeClick Callback invoked when a recipe is clicked.
 * @param onBookmarkClick Callback invoked when the bookmark icon is toggled.
 * @param selectedRecipeCategory The current category used for displaying recipes.
 * @param setRecipeCategory Callback to update the current category selection.
 * @param navigateBack Callback invoked to return to the previous screen or pane.
 * @param setSortingCategory Callback to update the recipe sorting criteria.
 * @param onDietSelected Callback to update the list of selected diet filters.
 * @param selectedDiets The list of currently active diet filters.
 * @param selectedRecipeSortingCategory The current criteria used for sorting recipes.
 * @param pagingItemsMap Recipes for each category.
 */
@Composable
internal fun RecipesListPane(
    scope: CoroutineScope,
    modifier: Modifier = Modifier,
    bookmarkedRecipeIds: List<Int>,
    onRecipeClick: (Recipe) -> Unit,
    onBookmarkClick: (Recipe) -> Unit,
    selectedRecipeCategory: RecipeCategory,
    setRecipeCategory: (RecipeCategory) -> Unit,
    navigateBack: () -> Unit,
    setSortingCategory: (RecipeSortingCategory) -> Unit,
    onDietSelected: (List<Diet>) -> Unit,
    selectedDiets: List<Diet>,
    selectedRecipeSortingCategory: RecipeSortingCategory,
    pagingItemsMap: Map<RecipeCategory, LazyPagingItems<Recipe>>

    ) {
    val haptic = LocalHapticFeedback.current

    val searchQuery = remember { mutableStateOf("") }

    val showFilterSheet = remember { mutableStateOf(false) }


    val scrollStates = RecipeCategory.entries.associateWith {
        rememberLazyListState()
    }
    val lazyListState = scrollStates[selectedRecipeCategory]!!
    val showScrollToTopButton by remember{
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0
        }
    }


    Scaffold(
        topBar = {
            RecipeListTopAppBar(
                navigateBack,
                onShowFilterSheet = { showFilterSheet.value = true })
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTopButton,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                ScrollToTopButtonFAB(
                    onClick = {
                        scope.launch {
                            lazyListState.animateScrollToItem(0)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        RecipeTabs(
            selectedRecipeCategory = selectedRecipeCategory,
            onTabSelected = setRecipeCategory,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                )
        )
        { recipeCategoryForPage ->
            val recipes = pagingItemsMap[recipeCategoryForPage]!!
            val lazyListState = scrollStates[recipeCategoryForPage]!!

            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    StandardSearchBar(
                        query = searchQuery.value,
                        onQueryChange = {
                            searchQuery.value = it
                        },
                        onSearch = {
                            TODO()
                        },
                    )
                }
                // Initial Load Shimmer (only if list is empty)
                if (recipes.loadState.refresh is LoadState.Loading && recipes.itemCount == 0) {
                    items(3) {
                        RecipeItemShimmer()
                    }
                }

                items(
                    count = recipes.itemCount,
                    key = recipes.itemKey { it.id },
                ) { index ->

                    val recipe = recipes[index]
                    if (recipe != null) {
                        RecipeCard(
                            recipe = recipe,
                            onRecipeClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onRecipeClick(it) },
                            isBookmarked = recipe.id in bookmarkedRecipeIds,
                            onBookmarkClick = {
                                onBookmarkClick(it)
                            }
                        )
                    } else {
                        // Placeholder during fetching
                        RecipeItemShimmer()
                    }

                }
                // 4. Pagination (Append) Shimmer
                if (recipes.loadState.append is LoadState.Loading) {
                    item {
                        RecipeItemShimmer()
                    }
                }
            }
        }
        if (showFilterSheet.value) {
            FilterAndSortSheet(
                selectedDiets = selectedDiets,
                selectedRecipeSortingCategory = selectedRecipeSortingCategory,
                onCloseFilterSheet = { showFilterSheet.value = false },
                onDietSelected = onDietSelected,
                onRecipeSortingCategorySelected = setSortingCategory,
                onResetFilters = {
                    onDietSelected(emptyList())
                    setSortingCategory(RecipeSortingCategory.POPULARITY)
                },

            )
        }
    }

}

@Preview(device = "id:pixel_7", showBackground = true)
@Composable
fun RecipesListPanePreview() {
    DeliveryAppTheme {
        val pagingItemsMap = RecipeCategory.entries.associateWith {
            flowOf(PagingData.from(listOf(steakRecipe, spaghettiRecipe, steakRecipe.copy(id = 3))))
                .collectAsLazyPagingItems()
        }
        RecipesListPane(
            scope = rememberCoroutineScope(),
            modifier = Modifier.fillMaxSize(),
            bookmarkedRecipeIds = listOf(1),
            onRecipeClick = {},
            onBookmarkClick = {},
            selectedRecipeCategory = RecipeCategory.DINNER,
            setRecipeCategory = {},
            navigateBack = {},
            setSortingCategory = {},
            onDietSelected = {},
            selectedDiets = emptyList(),
            selectedRecipeSortingCategory = RecipeSortingCategory.POPULARITY,
            pagingItemsMap = pagingItemsMap
        )
    }
}

@Preview(device = "id:pixel_7", showBackground = true)
@Composable
fun RecipeDetailPanePreview() {
    DeliveryAppTheme {
        RecipeDetailPane(
            recipe = steakRecipe,
            scope = rememberCoroutineScope(),
            isBookmarked = true,
            onBookmarkClick = {},
            navigateBack = {},
            addRecipeToShoppingCart = {},
            deleteRecipeFromShoppingCart = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}



