package com.example.deliveryapp.feature.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.deliveryapp.core.data.model.CartProduct
import com.example.deliveryapp.core.designsystem.component.CartItem
import com.example.deliveryapp.core.designsystem.component.CheckoutTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.deliveryapp.R
import com.example.deliveryapp.core.data.model.Product
import com.example.deliveryapp.core.data.model.fritzKolaBox033
import com.example.deliveryapp.core.data.model.fritzKolaBox05
import com.example.deliveryapp.core.data.model.fritzKolaSingle033
import com.example.deliveryapp.core.data.model.fritzKolaSingle05
import com.example.deliveryapp.core.designsystem.component.CheckoutFooter
import com.example.deliveryapp.core.designsystem.component.EmptyCheckoutTopAppBar
import com.example.deliveryapp.core.designsystem.component.LookAtOrderButton
import com.example.deliveryapp.core.designsystem.component.ProductDetail
import com.example.deliveryapp.core.designsystem.component.ProductDetailTopAppBar
import com.example.deliveryapp.core.designsystem.component.ResumeShoppingButton
import com.example.deliveryapp.core.designsystem.component.StartShoppingButton
import com.example.deliveryapp.core.designsystem.theme.DeliveryAppTheme
import kotlinx.coroutines.delay

/**
 * Entry point for the checkout screen, responsible for initializing the [CheckoutViewModel]
 * and collecting its UI state.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param viewModel The [CheckoutViewModel] that manages the business logic and state for the checkout.
 * @param navigateBack Callback to navigate to the previous screen.
 * @param navigateToProducts Callback to navigate back to the product list.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun CheckoutRoute(
    modifier: Modifier = Modifier,
    viewModel: CheckoutViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToProducts: () -> Unit
) {
    CheckoutScreen(
        modifier = modifier,
        navigateBack = navigateBack,
        navigateToProducts = navigateToProducts,
        cartItems = viewModel.cartItems.collectAsStateWithLifecycle().value,
        deleteCartItem = viewModel::deleteProductFromCart,
        decreaseQuantityOfCartItem = viewModel::decreaseQuantityOfCartItem,
        increaseQuantityOfCartItem = viewModel::increaseQuantityOfCartItem,
        favoriteProductsIds = viewModel.favoriteProductsIds.collectAsStateWithLifecycle().value,
        setProductFavorite = viewModel::setProductFavorite,
        clearShoppingCart = viewModel::clearShoppingCart,
        totalPrice = viewModel.totalPrice.collectAsStateWithLifecycle().value,
        totalDeposit = viewModel.totalDeposit.collectAsStateWithLifecycle().value,
        serviceFee = viewModel.serviceFee.collectAsStateWithLifecycle().value,
        purchaseCompleted = viewModel.purchaseCompleted.collectAsStateWithLifecycle().value,
        onPurchase = viewModel::onPurchase,
        showPurchaseCompletedScreen = viewModel.showPurchaseCompletedScreen.collectAsStateWithLifecycle().value
    )
}


/**
 * The main UI component for the checkout screen that handles the adaptive layout
 * between a list of cart items and product details.
 *
 * This composable uses a [NavigableListDetailPaneScaffold] to provide a responsive
 * UI that displays either the cart list, an empty state, a purchase confirmation,
 * or a split-view with product details depending on the screen size and state.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param navigateBack Callback to navigate to the previous screen.
 * @param navigateToProducts Callback to navigate to the product catalog.
 * @param cartItems The list of products currently in the user's shopping cart.
 * @param decreaseQuantityOfCartItem Callback to decrement the quantity of a specific product by ID.
 * @param deleteCartItem Callback to remove a specific product from the cart by ID.
 * @param increaseQuantityOfCartItem Callback to increment the quantity of a specific product by ID.
 * @param favoriteProductsIds List of IDs for products marked as favorites.
 * @param setProductFavorite Callback to toggle the favorite status of a product.
 * @param clearShoppingCart Callback to remove all items from the cart.
 * @param totalPrice The subtotal cost of all items in the cart.
 * @param totalDeposit The total deposit/pfand value for the items in the cart.
 * @param serviceFee The additional fee applied to the order.
 * @param purchaseCompleted Boolean indicating if the current purchase process is finished.
 * @param showPurchaseCompletedScreen Boolean determining if the "Thank You" screen should be displayed.
 * @param onPurchase Callback to trigger the final purchase transaction.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun CheckoutScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToProducts: () -> Unit,
    cartItems: List<CartProduct>,
    decreaseQuantityOfCartItem: (Int) -> Unit,
    deleteCartItem: (Int) -> Unit,
    increaseQuantityOfCartItem: (Int) -> Unit,
    favoriteProductsIds: List<Int>,
    setProductFavorite: (Product) -> Unit,
    clearShoppingCart: () -> Unit,
    totalPrice: Int,
    totalDeposit: Int,
    serviceFee: Int,
    purchaseCompleted: Boolean,
    showPurchaseCompletedScreen: Boolean,
    onPurchase: () -> Unit
) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<Product>()
    val scope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                when {
                    (cartItems.isEmpty() && !showPurchaseCompletedScreen) ->
                        EmptyCheckoutScreen(
                        navigateBack = navigateBack,
                        navigateToProducts = navigateToProducts)
                    (cartItems.isEmpty()) ->
                        PurchaseCompletedScreen(
                            navigateBack = navigateBack,
                            navigateToProducts = navigateToProducts)
                    else ->
                        CheckoutListPane(
                        onProductClick = { product ->
                            // Navigate to the detail pane with the passed product
                            scope.launch {
                                scaffoldNavigator.navigateTo(
                                    ListDetailPaneScaffoldRole.Detail,
                                    product
                                )
                            }
                        },
                        scope = scope,
                        cartItems = cartItems,
                        decreaseQuantityOfCartItem = decreaseQuantityOfCartItem,
                        deleteCartItem = deleteCartItem,
                        increaseQuantityOfCartItem = increaseQuantityOfCartItem,
                        clearShoppingCart = clearShoppingCart,
                        navigateBack = navigateBack,
                        totalPrice = totalPrice,
                        totalDeposit = totalDeposit,
                        serviceFee = serviceFee,
                        purchaseCompleted = purchaseCompleted,
                        onPurchase = onPurchase,
                        favoriteProductsIds = favoriteProductsIds,
                        setProductFavorite = setProductFavorite
                    )
                }
            }
        },
        detailPane = {
            AnimatedPane {
                // Show the detail pane content if selected product is available
                scaffoldNavigator.currentDestination?.contentKey?.let {
                    CheckoutDetailPane(
                        scope = scope,
                        product = it,
                        isFavorite = it.id in favoriteProductsIds,
                        onAddToFavoritesClick =
                            setProductFavorite,
                        navigateBack = {
                            scope.launch {
                                scaffoldNavigator.navigateBack()
                            }
                        },
                        increaseQuantityOfCartItem = increaseQuantityOfCartItem,
                        decreaseQuantityOfCartItem = decreaseQuantityOfCartItem,
                        cartItems = cartItems,
                        favoriteProductsIds = favoriteProductsIds,
                        )
                }
            }
        },
    )

}

/**
 * A detail pane for the checkout screen that displays comprehensive information about a specific product.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param isFavorite Boolean indicating if the current product is marked as a favorite.
 * @param cartItems The current list of products in the shopping cart to determine product quantity.
 * @param favoriteProductsIds List of IDs for products currently marked as favorites.
 * @param decreaseQuantityOfCartItem Callback to decrement the quantity of the product in the cart.
 * @param product The [Product] object containing the details to be displayed.
 * @param scope A [CoroutineScope] used for launching snackbars and other asynchronous UI actions.
 * @param navigateBack Callback to return to the previous view (e.g., the list pane).
 * @param onAddToFavoritesClick Callback to toggle the favorite status for the given product.
 * @param increaseQuantityOfCartItem Callback to increment the quantity of the product in the cart.
 */
@Composable
internal fun CheckoutDetailPane(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    cartItems: List<CartProduct>,
    favoriteProductsIds: List<Int>,
    decreaseQuantityOfCartItem: (Int) -> Unit,
    product: Product,
    scope: CoroutineScope,
    navigateBack: () -> Unit,
    onAddToFavoritesClick: (Product) -> Unit,
    increaseQuantityOfCartItem: (Int) -> Unit
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
                onAddToFavoritesClick(it)
            },
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
            cartItems = cartItems,
            favoriteProductsIds = favoriteProductsIds,
            onIncrementQuantity = increaseQuantityOfCartItem,
            onDecrementQuantity = decreaseQuantityOfCartItem,


            )
    }
}

/**
 * Displays the list of items in the shopping cart along with order summary and purchase actions.
 *
 * This pane includes a scrollable list of [CartItem]s, a top bar with navigation and
 * clear-cart functionality, and a footer displaying the price breakdown and purchase button.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param scope Coroutine scope for handling snackbar messages and async actions.
 * @param cartItems The list of [CartProduct] items currently in the cart.
 * @param favoriteProductsIds List of IDs for products marked as favorites.
 * @param setProductFavorite Callback to toggle the favorite status of a product.
 * @param decreaseQuantityOfCartItem Callback to decrement the quantity of a product.
 * @param onProductClick Callback triggered when a product item is selected.
 * @param deleteCartItem Callback to remove a specific product from the cart.
 * @param increaseQuantityOfCartItem Callback to increment the quantity of a product.
 * @param clearShoppingCart Callback to remove all items from the cart.
 * @param navigateBack Callback to navigate to the previous screen.
 * @param totalPrice The subtotal cost of the items.
 * @param totalDeposit The total deposit/pfand value for the items.
 * @param serviceFee The additional fee applied to the order.
 * @param purchaseCompleted Boolean indicating if the purchase process is successful.
 * @param onPurchase Callback to initiate the purchase transaction.
 */
@Composable
internal fun CheckoutListPane(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    cartItems: List<CartProduct>,
    favoriteProductsIds: List<Int>,
    setProductFavorite: (Product) -> Unit,
    decreaseQuantityOfCartItem: (Int) -> Unit,
    onProductClick: (Product) -> Unit,
    deleteCartItem: (Int) -> Unit,
    increaseQuantityOfCartItem: (Int) -> Unit,
    clearShoppingCart: () -> Unit,
    navigateBack: () -> Unit,
    totalPrice: Int,
    totalDeposit: Int,
    serviceFee: Int,
    purchaseCompleted: Boolean,
    onPurchase: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CheckoutTopAppBar(navigateBack) {
                clearShoppingCart()
            }
        },
    ) { innerPadding ->
        val lazyListState = rememberLazyListState()


        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(
                12.dp
            ),
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
            itemsIndexed(
                items = cartItems,
                key = { _, cartItem -> cartItem.product.id }
            ) { index, cartItem ->

                if (index > 0) HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
                CartItem(
                    modifier = Modifier.animateItem(),
                    product = cartItem.product,
                    isFavorite = cartItem.product.id in favoriteProductsIds,
                    onAddToFavoritesClick = { setProductFavorite(cartItem.product) },
                    productQuantity = cartItem.countInCart,
                    onProductClick = onProductClick,
                    onIncreaseQuantity = { increaseQuantityOfCartItem(cartItem.product.id) },
                    onDecreaseQuantity = { decreaseQuantityOfCartItem(cartItem.product.id) },
                    onDelete = {
                        scope.launch {
                            deleteCartItem(cartItem.product.id)
                            val result = snackbarHostState.showSnackbar(context.getString(R.string.product_removed_from_cart), actionLabel = context.getString(R.string.undone), duration = SnackbarDuration.Short)
                            when (result) {
                                SnackbarResult.ActionPerformed -> {
                                    scope.launch {
                                        increaseQuantityOfCartItem(cartItem.product.id)
                                    }
                                }
                                SnackbarResult.Dismissed -> {}
                            }
                        }
                    },
                )
            }
            item {
                CheckoutFooter(
                    totalPrice = totalPrice,
                    totalDeposit = totalDeposit,
                    serviceFee = serviceFee,
                    onPurchase = onPurchase,
                    purchaseCompleted = purchaseCompleted,
                )
            }
        }

    }
}

/**
 * Displays a screen indicating that the shopping cart is currently empty.
 *
 * This component informs the user that no items have been added to the checkout yet
 * and provides a call-to-action to return to the product catalog.
 *
 * @param navigateBack Callback to navigate to the previous screen.
 * @param navigateToProducts Callback to navigate to the product list to start shopping.
 */
@Composable
fun EmptyCheckoutScreen(
    navigateBack: () -> Unit,
    navigateToProducts: () -> Unit
) {
    Scaffold(
        topBar = {
            EmptyCheckoutTopAppBar(navigateBack)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 48.dp)
                .consumeWindowInsets(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.empty_cart),
                contentDescription = stringResource(id = R.string.empty_checkout)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = stringResource(id = R.string.empty_checkout),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = stringResource(id = R.string.find_something_new),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(24.dp))
            StartShoppingButton(onClick = navigateToProducts)
        }
    }

}

/**
 * Displays a confirmation screen to the user after a successful purchase.
 *
 * This screen provides feedback that the order was received, showing a success illustration,
 * a thank-you message, and options to either continue shopping or view the order status.
 *
 * @param navigateBack Callback to navigate to the previous screen or order details.
 * @param navigateToProducts Callback to navigate the user back to the product catalog.
 */
@Composable
fun PurchaseCompletedScreen(
    navigateBack: () -> Unit,
    navigateToProducts: () -> Unit
) {
    Scaffold(
        topBar = {
            EmptyCheckoutTopAppBar(navigateBack)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 48.dp)
                .consumeWindowInsets(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.order_successful),
                contentDescription = stringResource(id = R.string.order_successful)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = stringResource(id = R.string.thank_you_for_ordering),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = stringResource(id = R.string.we_have_received_your_order),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(24.dp))
            ResumeShoppingButton(onClick = navigateToProducts)
            Spacer(modifier = Modifier.height(24.dp))
            LookAtOrderButton(onClick = navigateBack)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyCheckoutScreenPreview() {
    DeliveryAppTheme {
        EmptyCheckoutScreen ({}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun PurchaseCompletedScreenPreview() {
    DeliveryAppTheme {
        PurchaseCompletedScreen ({}, {})
    }
}



@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {

    val cartItems = listOf(
        CartProduct(fritzKolaSingle033, 1),
        CartProduct(fritzKolaBox033, 2),
        CartProduct(fritzKolaBox05, 1),
        CartProduct(fritzKolaSingle05, 1)
    )
    val favoriteProductsIds = listOf(1, 3)
    val totalPrice =
        fritzKolaSingle033.price + fritzKolaBox033.price * 2 + fritzKolaBox05.price + fritzKolaSingle05.price
    val totalDeposit =
        fritzKolaSingle033.deposit + fritzKolaBox033.deposit * 2 + fritzKolaBox05.deposit + fritzKolaSingle05.deposit

    var isPaymentCompleted by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()



    DeliveryAppTheme {
        CheckoutScreen(
            cartItems = cartItems,
            decreaseQuantityOfCartItem = {},
            deleteCartItem = {},
            increaseQuantityOfCartItem = {},
            favoriteProductsIds = favoriteProductsIds,
            setProductFavorite = {},
            clearShoppingCart = {},
            totalPrice = totalPrice,
            totalDeposit = totalDeposit,
            serviceFee = 199,
            purchaseCompleted = isPaymentCompleted,
            onPurchase = {
                coroutineScope.launch {
                    delay(2000)
                    isPaymentCompleted = true
                }
            },
            navigateBack = {},
            navigateToProducts = {},
            showPurchaseCompletedScreen = false

        )
    }
}


@Preview(showBackground = true)
@Composable
fun CheckoutDetailPreview() {
    DeliveryAppTheme {
        val isFavorite = remember { mutableStateOf(false) }
        var quantityInCart by remember { mutableIntStateOf(0) }
        val cartItems = remember(quantityInCart) {
            listOf(CartProduct(fritzKolaSingle033, quantityInCart))
        }

        CheckoutDetailPane(
            product = fritzKolaSingle033,
            isFavorite = isFavorite.value,
            navigateBack = {},
            onAddToFavoritesClick = { isFavorite.value = !isFavorite.value },
            scope = rememberCoroutineScope(),
            increaseQuantityOfCartItem = { quantityInCart++ },
            decreaseQuantityOfCartItem = { quantityInCart-- },
            cartItems = cartItems,
            favoriteProductsIds = listOf(),
        )
    }
}

