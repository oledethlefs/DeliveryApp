package com.example.deliveryapp.feature.products

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
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
import com.example.deliveryapp.core.data.model.CartProduct
import com.example.deliveryapp.core.data.model.Diet
import com.example.deliveryapp.core.data.model.Product
import com.example.deliveryapp.core.data.model.ProductCategory
import com.example.deliveryapp.core.data.model.ProductFilter
import com.example.deliveryapp.core.data.model.ProductSortingCategory
import com.example.deliveryapp.core.data.model.fritzKolaBox033
import com.example.deliveryapp.core.data.model.fritzKolaBox05
import com.example.deliveryapp.core.data.model.fritzKolaSingle033
import com.example.deliveryapp.core.data.model.fritzKolaSingle05
import com.example.deliveryapp.core.designsystem.component.FilterAndSortSheet
import com.example.deliveryapp.core.designsystem.component.ProductCategory
import com.example.deliveryapp.core.designsystem.component.ProductCategoryTopAppBar
import com.example.deliveryapp.core.designsystem.component.ProductDetail
import com.example.deliveryapp.core.designsystem.component.ProductDetailTopAppBar
import com.example.deliveryapp.core.designsystem.component.ProductItem
import com.example.deliveryapp.core.designsystem.component.ProductItemShimmer
import com.example.deliveryapp.core.designsystem.component.ProductsTopAppBar
import com.example.deliveryapp.core.designsystem.component.ScrollToTopButtonFAB
import com.example.deliveryapp.core.designsystem.component.ShimmerEffect
import com.example.deliveryapp.core.designsystem.component.StandardSearchBar
import com.example.deliveryapp.core.designsystem.theme.DeliveryAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import com.example.deliveryapp.feature.products.ProductsViewModel.CategoryUiState

/**
 * Entry point for the products screen, responsible for initializing the [ProductsViewModel].
 *
 *
 * @param modifier Modifier to be applied to the product screen layout.
 * @param viewModel The [ProductsViewModel] that provides data and handles business logic.
 * @param navigateBack Callback to be invoked when the user navigates back from the root category.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ProductsRoute(
    modifier: Modifier = Modifier,
    viewModel: ProductsViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    ProductsScreen(
        modifier = modifier,
        navigateBack = navigateBack,
        products = viewModel.products.collectAsLazyPagingItems(),
        addProductToCart = viewModel::addProductToCart,
        favoriteProductsIds = viewModel.favoriteProductsIds.collectAsStateWithLifecycle().value,
        setProductFavorite = viewModel::setProductFavorite,
        setProductCategoryId = viewModel::setProductCategoryId,
        selectedDiets = viewModel.selectedDiets.collectAsStateWithLifecycle().value,
        onDietSelected = viewModel::updateSelectedDiets,
        selectedProductSortingCategory = viewModel.sortingCategory.collectAsStateWithLifecycle().value,
        setSortingCategory = viewModel::setSortingCategory,
        productFilters = viewModel.productFilters.collectAsStateWithLifecycle().value,
        selectedProductFilters = viewModel.selectedProductFilters.collectAsStateWithLifecycle().value,
        setFilter = viewModel::toggleProductFilter,
        cartItems = viewModel.cartItems.collectAsStateWithLifecycle().value,
        onIncrementQuantity = viewModel::increaseQuantityOfCartItem,
        onDecrementQuantity = viewModel::decreaseQuantityOfCartItem,
        uiState = viewModel.categoryUiState.collectAsStateWithLifecycle().value

    )
}

/**
 * The main UI component for the products screen that handles the adaptive layout for displaying
 * product categories, product lists, and product details.
 *
 *
 * @param modifier Modifier to be applied to the layout.
 * @param navigateBack Callback to navigate away from the products feature.
 * @param uiState The current state of product categories (Loading, Success, or Error).
 * @param products The paginated list of products to display when a leaf category is selected.
 * @param addProductToCart Callback to add a specific [Product] to the shopping cart.
 * @param favoriteProductsIds List of IDs for products marked as favorites by the user.
 * @param setProductFavorite Callback to toggle the favorite status of a [Product].
 * @param setProductCategoryId Callback to update the current category selection.
 * @param selectedDiets List of currently active diet filters.
 * @param onDietSelected Callback to update the list of selected diet filters.
 * @param selectedProductSortingCategory The current criteria used for sorting products.
 * @param setSortingCategory Callback to update the product sorting criteria.
 * @param productFilters Available filters for the current product context.
 * @param selectedProductFilters List of currently active product filters.
 * @param setFilter Callback to toggle a specific [ProductFilter].
 * @param cartItems List of products currently in the user's cart.
 * @param onIncrementQuantity Callback to increase the quantity of a cart item by its ID.
 * @param onDecrementQuantity Callback to decrease the quantity of a cart item by its ID.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun ProductsScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    uiState: CategoryUiState,
    products: LazyPagingItems<Product>,
    addProductToCart: (Product) -> Unit,
    favoriteProductsIds: List<Int>,
    setProductFavorite: (Product) -> Unit,
    setProductCategoryId: (Int) -> Unit,
    selectedDiets: List<Diet>,
    onDietSelected: (List<Diet>) -> Unit,
    selectedProductSortingCategory: ProductSortingCategory,
    setSortingCategory: (ProductSortingCategory) -> Unit,
    productFilters: List<ProductFilter>,
    selectedProductFilters: List<ProductFilter>,
    setFilter: (ProductFilter) -> Unit,
    cartItems: List<CartProduct>,
    onIncrementQuantity: (Int) -> Unit,
    onDecrementQuantity: (Int) -> Unit,

    ) {


    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<Product>()
    val scope = rememberCoroutineScope()

    val navigateBack: () -> Unit = {
        if (scaffoldNavigator.canNavigateBack()) {
            scope.launch { scaffoldNavigator.navigateBack() }
        } else if (uiState is CategoryUiState.Success && uiState.category.id != 0) {
            setProductCategoryId(uiState.category.parentCategoryId ?: 0)
        } else {
            navigateBack()
        }
    }

    BackHandler(enabled = true) {
        navigateBack()
    }

    val categoryScrollStates = rememberSaveable(saver = stateMapSaver()) {
        mutableMapOf()
    }

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                AnimatedContent(
                    targetState = uiState,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.92f))
                            .togetherWith(fadeOut(animationSpec = tween(200)))
                    },
                    label = "CategoryTransition"
                ) { state ->
                    when (state) {
                        // Server is fetching first product category
                        is CategoryUiState.Loading -> {
                            ProductCategoriesListPane(
                                productCategory = null,
                                navigateBack = navigateBack,
                                onCategoryClick = { },

                            )
                        }

                        is CategoryUiState.Success -> {
                            val category = state.category
                            if (category.childrenProductCategories.isEmpty()) {
                                ProductListPane(
                                    scope = scope,
                                    navigateBack = navigateBack,
                                    productCategoryName = category.name,
                                    products = products,
                                    cartItems = cartItems,
                                    onIncrementQuantity = onIncrementQuantity,
                                    onDecrementQuantity = onDecrementQuantity,
                                    favoriteProductsIds = favoriteProductsIds,
                                    setProductFavorite = setProductFavorite,
                                    onProductClick = { product ->
                                        // Navigate to the detail pane with the passed product
                                        scope.launch {
                                            scaffoldNavigator.navigateTo(
                                                ListDetailPaneScaffoldRole.Detail,
                                                product
                                            )
                                        }
                                    },
                                    selectedDiets = selectedDiets,
                                    onDietSelected = onDietSelected,
                                    selectedProductSortingCategory = selectedProductSortingCategory,
                                    setSortingCategory = setSortingCategory,
                                    productFilters = productFilters,
                                    selectedProductFilters = selectedProductFilters,
                                    setFilter = setFilter,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                val state = categoryScrollStates.getOrPut(category.id) {
                                    LazyGridState()
                                }
                                ProductCategoriesListPane(
                                    productCategory = category,
                                    lazyGridState = state,
                                    onCategoryClick = {
                                        scope.launch {
                                            setProductCategoryId(it.id)
                                        }
                                    },
                                    navigateBack = navigateBack
                                )
                            }
                        }

                        is CategoryUiState.Error -> {}
                    }
                }


            }
        },
        detailPane = {
            AnimatedPane {
                // Show the detail pane content if selected product is available
                scaffoldNavigator.currentDestination?.contentKey?.let {
                    ProductDetailPane(
                        scope = scope,
                        product = it,
                        cartItems = cartItems,
                        onIncrementQuantity = onIncrementQuantity,
                        onDecrementQuantity = onDecrementQuantity,
                        isFavorite = it.id in favoriteProductsIds,
                        navigateBack = navigateBack,
                        addProductToShoppingCart = addProductToCart,
                        onFavoriteClick = setProductFavorite,
                        favoriteProductsIds = favoriteProductsIds,
                        modifier = modifier,

                        )
                }
            }
        },
    )
}

/**
 * A pane that displays a grid of product categories.
 *
 * This composable handles both the loading state (displaying shimmer effects) and the
 * success state (displaying a grid of [ProductCategory] items). It includes a top app bar
 * for navigation and manages its own scrolling state via [LazyGridState].
 *
 * @param modifier Modifier to be applied to the layout.
 * @param productCategory The parent category containing the children categories to display.
 * If null, the shimmer loading state is shown.
 * @param lazyGridState The state object to be used to control or observe the grid's scroll position.
 * @param onCategoryClick Callback invoked when a specific category is selected.
 * @param navigateBack Callback invoked when the user presses the back button in the top bar.
 */
@Composable
internal fun ProductCategoriesListPane(
    modifier: Modifier = Modifier,
    productCategory: ProductCategory?,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    onCategoryClick: (ProductCategory) -> Unit,
    navigateBack: () -> Unit
) {

    Scaffold(
        topBar = {
            ProductCategoryTopAppBar(navigateBack)
        }
    ) { innerPadding ->

        if (productCategory == null) {
            LazyVerticalGrid(
                state = lazyGridState,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            ) {
                items(6) {
                    ShimmerEffect(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color.LightGray, RoundedCornerShape(12.dp))
                    )
                }
            }
            return@Scaffold
        } else {
            LazyVerticalGrid(
                state = lazyGridState,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    )
            ) {
                items(
                    items = productCategory.childrenProductCategories,
                    key = { it.id }
                ) { category ->
                    ProductCategory(
                        category = category,
                        onCategoryClick = { onCategoryClick(category) }
                    )
                }
            }
        }
    }
}

/**
 * A composable pane that displays a list of [Product]s within a specific [ProductCategory].
 *
 * @param scope The [CoroutineScope] used for launching snack bars and scroll animations.
 * @param navigateBack Callback to be invoked when the user clicks the back button.
 * @param productCategoryName The name of the current category to display in the top bar.
 * @param products The paginated list of products to be displayed.
 * @param cartItems The current list of items in the shopping cart to reflect quantities.
 * @param onIncrementQuantity Callback to increase the quantity of a product in the cart.
 * @param onDecrementQuantity Callback to decrease the quantity of a product in the cart.
 * @param favoriteProductsIds A list of IDs representing products currently marked as favorites.
 * @param setProductFavorite Callback to toggle the favorite status of a product.
 * @param onProductClick Callback to be invoked when a product item is clicked.
 * @param selectedDiets The currently active dietary filters.
 * @param onDietSelected Callback to update the selected dietary filters.
 * @param selectedProductSortingCategory The currently active sorting criterion.
 * @param setSortingCategory Callback to update the sorting category.
 * @param productFilters The available list of product filters (e.g., brands).
 * @param selectedProductFilters The currently active product filters.
 * @param setFilter Callback to toggle a specific product filter.
 * @param modifier Modifier to be applied to the pane layout.
 */
@Composable
internal fun ProductListPane(
    scope: CoroutineScope,
    navigateBack: () -> Unit,
    productCategoryName: String,
    products: LazyPagingItems<Product>,
    cartItems: List<CartProduct>,
    onIncrementQuantity: (Int) -> Unit,
    onDecrementQuantity: (Int) -> Unit,
    favoriteProductsIds: List<Int>,
    setProductFavorite: (Product) -> Unit,
    onProductClick: (Product) -> Unit,
    selectedDiets: List<Diet>,
    onDietSelected: (List<Diet>) -> Unit,
    selectedProductSortingCategory: ProductSortingCategory,
    setSortingCategory: (ProductSortingCategory) -> Unit,
    productFilters: List<ProductFilter>,
    selectedProductFilters: List<ProductFilter>,
    setFilter: (ProductFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current

    val searchQuery = remember { mutableStateOf("") }

    val showFilterSheet = remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val showScrollToTopButton by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0
        }
    }


    Scaffold(
        topBar = {
            ProductsTopAppBar(
                navigateBack,
                productCategoryName = productCategoryName,
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
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
            state = lazyListState,
            contentPadding = PaddingValues(12.dp),
        ) {
            item {
                StandardSearchBar(
                    query = searchQuery.value,
                    onQueryChange = { searchQuery.value = it },
                    onSearch = { TODO() },
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            // 1. Initial Load Shimmer
            if (products.loadState.refresh == LoadState.Loading || products.itemCount == 0) {
                items(5) { index ->
                    if (index > 0) HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
                    ProductItemShimmer()
                }

            }
            // 2. Main Content
            items(
                count = products.itemCount,
                key = products.itemKey { it.id },
            ) { index ->

                val product = products[index]


                if (index > 0) HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

                if (product != null) {
                    ProductItem(
                        product = product,
                        favoriteProductsIds = favoriteProductsIds,
                        cartItems = cartItems,
                        onIncrementQuantity = { onIncrementQuantity(product.id) },
                        onDecrementQuantity = { onDecrementQuantity(product.id) },
                        onAddToFavoritesClick = {
                            scope.launch {
                                setProductFavorite(it)
                            }


                        },
                        onProductClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onProductClick(it)
                        },
                    )
                } else {
                    ProductItemShimmer()
                }


            }

            // 3. Append (Pagination) Shimmer
            if (products.loadState.append is LoadState.Loading) {
                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
                    ProductItemShimmer()
                }
            }
        }
    }
    if (showFilterSheet.value) {
        FilterAndSortSheet(
            selectedDiets = selectedDiets,
            selectedProductSortingCategory = selectedProductSortingCategory,
            selectedProductFilters = selectedProductFilters,
            productFilters = productFilters,
            onProductFilterSelected = setFilter,
            onCloseFilterSheet = { showFilterSheet.value = false },
            onDietSelected = onDietSelected,
            onProductSortingCategorySelected = setSortingCategory,
            onResetFilters = {
                onDietSelected(emptyList())
                setSortingCategory(ProductSortingCategory.POPULARITY)
            }
        )
    }
}


/**
 * Displays the detailed information for a specific product using [ProductDetail].
 *
 *
 * @param modifier Modifier to be applied to the detail pane layout.
 * @param product The [Product] entity containing the data to be displayed.
 * @param cartItems The current list of [CartProduct]s to determine quantities already in the cart.
 * @param favoriteProductsIds A list of IDs for products that have been favorited by the user.
 * @param onIncrementQuantity Callback to increase the quantity of this product in the cart.
 * @param onDecrementQuantity Callback to decrease the quantity of this product in the cart.
 * @param isFavorite Boolean flag indicating if the current product is marked as a favorite.
 * @param navigateBack Callback to be invoked when the user clicks the back button in the top bar.
 * @param addProductToShoppingCart Callback to add the current product to the shopping cart.
 * @param onFavoriteClick Callback to toggle the favorite status of the product.
 * @param scope The [CoroutineScope] used for launching snackbar messages and other side effects.
 */
@Composable
internal fun ProductDetailPane(
    modifier: Modifier = Modifier,
    product: Product,
    cartItems: List<CartProduct>,
    favoriteProductsIds: List<Int>,
    onIncrementQuantity: (Int) -> Unit,
    onDecrementQuantity: (Int) -> Unit,
    isFavorite: Boolean,
    navigateBack: () -> Unit,
    addProductToShoppingCart: (Product) -> Unit,
    onFavoriteClick: (Product) -> Unit,
    scope: CoroutineScope,
) {
    Scaffold(
        topBar = {
            ProductDetailTopAppBar(navigateBack)
        },
    ) { innerPadding ->
        ProductDetail(
            product = product,
            isFavorite = isFavorite,
            setProductFavorite = {
                onFavoriteClick(it)
            },
            cartItems = cartItems,
            onIncrementQuantity = onIncrementQuantity,
            onDecrementQuantity = onDecrementQuantity,
            favoriteProductsIds = favoriteProductsIds,
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
 * Helper to save a Map of LazyListStates across process death/recompositions
 */
private fun stateMapSaver() = Saver<MutableMap<Int, LazyGridState>, Map<Int, List<Int>>>(
    save = { map ->
        map.mapValues {
            listOf(
                it.value.firstVisibleItemIndex,
                it.value.firstVisibleItemScrollOffset
            )
        }
    },
    restore = { savedMap ->
        val restored = mutableMapOf<Int, LazyGridState>()
        savedMap.forEach { (id, values) ->
            restored[id] = LazyGridState(values[0], values[1])
        }
        restored
    }
)

@Preview(device = "id:pixel_7", showBackground = true)
@Composable
fun ProductDetailPreview() {
    DeliveryAppTheme {
        val isFavorite = remember { mutableStateOf(false) }
        ProductDetailPane(
            product = fritzKolaSingle033,
            cartItems = emptyList(),
            onIncrementQuantity = {},
            onDecrementQuantity = {},
            isFavorite = isFavorite.value,
            navigateBack = {},
            addProductToShoppingCart = {},
            onFavoriteClick = { isFavorite.value = !isFavorite.value },
            scope = rememberCoroutineScope(),
            favoriteProductsIds = emptyList()
        )
    }
}

@Preview(device = "id:pixel_7", showBackground = true)
@Composable
fun ProductListPanePreview() {
    DeliveryAppTheme {
        val fakeProducts = listOf(
            fritzKolaSingle033,
            fritzKolaSingle05,
            fritzKolaBox033,
            fritzKolaBox05,
        )

        val productsFlow = flowOf(PagingData.from(fakeProducts))
        val lazyPagingItems = productsFlow.collectAsLazyPagingItems()

        val dummyFilters = listOf(
            ProductFilter(id = 1, name = "fritz", isBrandFilter = true),
            ProductFilter(id = 2, name = "Coca-Cola", isBrandFilter = true),
            ProductFilter(id = 3, name = "Sinalco", isBrandFilter = true)
        )

        val cartItems = listOf(
            CartProduct(
                product = fritzKolaSingle05,
                countInCart = 1
            )
        )


        ProductListPane(
            scope = rememberCoroutineScope(),
            navigateBack = {},
            productCategoryName = "Cola",
            products = lazyPagingItems,
            cartItems = cartItems,
            onIncrementQuantity = {},
            onDecrementQuantity = {},
            favoriteProductsIds = listOf(1),
            setProductFavorite = {},
            onProductClick = {},
            selectedDiets = emptyList(),
            onDietSelected = {},
            selectedProductSortingCategory = ProductSortingCategory.POPULARITY,
            setSortingCategory = {},
            productFilters = dummyFilters,
            selectedProductFilters = listOf(dummyFilters[0]),
            setFilter = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(device = "id:pixel_7", showBackground = true)
@Composable
fun ProductListPaneLoadingPreview() {
    DeliveryAppTheme {
        val productsFlow = flowOf(PagingData.empty<Product>())
        val lazyPagingItems = productsFlow.collectAsLazyPagingItems()

        val dummyFilters = listOf(
            ProductFilter(id = 1, name = "fritz", isBrandFilter = true),
            ProductFilter(id = 2, name = "Coca-Cola", isBrandFilter = true),
            ProductFilter(id = 3, name = "Sinalco", isBrandFilter = true)
        )


        ProductListPane(
            scope = rememberCoroutineScope(),
            navigateBack = {},
            productCategoryName = "Cola",
            products = lazyPagingItems,
            cartItems = emptyList(),
            onIncrementQuantity = {},
            onDecrementQuantity = {},
            favoriteProductsIds = listOf(1),
            setProductFavorite = {},
            onProductClick = {},
            selectedDiets = emptyList(),
            onDietSelected = {},
            selectedProductSortingCategory = ProductSortingCategory.POPULARITY,
            setSortingCategory = {},
            productFilters = dummyFilters,
            selectedProductFilters = listOf(dummyFilters[0]),
            setFilter = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_7")
@Composable
fun ProductCategoriesListPanePreview() {
    val mockSubCategories = listOf(
        ProductCategory(
            id = 1,
            name = "Backwaren",
            imageUrl = "android.resource://com.example.deliveryapp/" + R.drawable.carbs_category,
            childrenProductCategories = emptyList(),
            productFilters = emptyList()
        ),
        ProductCategory(
            id = 2,
            name = "Getränke",
            imageUrl = "android.resource://com.example.deliveryapp/" + R.drawable.drinks_category,
            childrenProductCategories = emptyList(),
            productFilters = emptyList()
        ),
        ProductCategory(
            id = 3,
            name = "Eier und Milchprodukte",
            imageUrl = "android.resource://com.example.deliveryapp/" + R.drawable.milk_eggs_category,
            childrenProductCategories = emptyList(),
            productFilters = emptyList()
        ),
        ProductCategory(
            id = 4,
            name = "Fleisch und Fisch",
            imageUrl = "android.resource://com.example.deliveryapp/" + R.drawable.meat_fish_category,
            childrenProductCategories = emptyList(),
            productFilters = emptyList()
        )
    )

    val mockParentCategory = ProductCategory(
        id = 0,
        name = "Alle Kategorien",
        imageUrl = "",
        childrenProductCategories = mockSubCategories,
        productFilters = emptyList()
    )

    DeliveryAppTheme {
        ProductCategoriesListPane(
            productCategory = mockParentCategory,
            onCategoryClick = {},
            navigateBack = {},
        )
    }
}





