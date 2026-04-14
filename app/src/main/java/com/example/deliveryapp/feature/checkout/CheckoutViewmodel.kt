package com.example.deliveryapp.feature.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deliveryapp.core.data.model.CartProduct
import com.example.deliveryapp.core.data.model.Product
import com.example.deliveryapp.core.data.repository.CheckoutRepository
import com.example.deliveryapp.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the checkout process and the state of the shopping cart.
 *
 * This class provides data streams for cart items, pricing totals, and purchase status.
 */
@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val checkoutRepository: CheckoutRepository
) : ViewModel() {

    private val _purchaseCompleted = MutableStateFlow(false)
    val purchaseCompleted = _purchaseCompleted.asStateFlow()

    private val _showPurchaseCompletedScreen = MutableStateFlow(false)
    val showPurchaseCompletedScreen = _showPurchaseCompletedScreen.asStateFlow()

    /**
     * A [StateFlow] representing the list of products currently in the user's shopping cart.
     * The flow is backed by the [checkoutRepository] and stays active for 5 seconds after
     * the last subscriber disappears.
     */
    val cartItems: StateFlow<List<CartProduct>> = checkoutRepository.productsInCart.
    stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    /**
     * A [StateFlow] representing the total quantity of all items currently in the shopping cart.
     */
    val totalCartItemsCount: StateFlow<Int> = cartItems.map { items ->
        items.sumOf { it.countInCart }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    /**
     * A [StateFlow] representing the list of unique identifiers for products marked as favorites by the user.
     * This flow is derived from [userDataRepository] and remains active for 5 seconds after the last
     * subscriber disappears.
     */
    val favoriteProductsIds: StateFlow<List<Int>> = userDataRepository.userData
        .map { it.favoriteProductsIds }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    /**
     * Toggles the favorite status of a specific product.
     *
     * @param product The [Product] to be marked or unmarked as a favorite.
     */
    fun setProductFavorite(product: Product) {
        val productId = product.id
        viewModelScope.launch {
            userDataRepository.setProductFavorite(productId)
        }
    }

    /**
     * A [StateFlow] representing the total price of all items currently in the shopping cart.
     */
    val totalPrice: StateFlow<Int> = cartItems.map { items ->
        items.sumOf { cartItem ->
            (cartItem.product.price * cartItem.countInCart)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )
    /**
     * A [StateFlow] representing the total accumulated deposit value for all products currently in the shopping cart.
     */
    val totalDeposit: StateFlow<Int> = cartItems.map { items ->
        items.sumOf { cartItem ->
            (cartItem.product.deposit * cartItem.countInCart)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    /**
     * A [StateFlow] representing the service fee applied to the order, retrieved from [checkoutRepository].
     */
    val serviceFee: StateFlow<Int> = checkoutRepository.serviceFee.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    fun onPurchase() {
        viewModelScope.launch {
            if (cartItems.value.isNotEmpty()) {
                delay(2000)

                _purchaseCompleted.value = true

                delay(2000)

                _showPurchaseCompletedScreen.value = true

                clearShoppingCart()
            }
        }
    }

    /**
     * Decreases the quantity of a specific product in the shopping cart.
     *
     * @param productId The unique identifier of the product whose quantity should be reduced.
     */
    fun decreaseQuantityOfCartItem(productId: Int) {
        viewModelScope.launch {
            checkoutRepository.decreaseQuantityOfCartItem(productId)
        }

    }

    /**
     * Removes a specific product from the shopping cart entirely, regardless of its quantity.
     *
     * @param productId The unique identifier of the product to be removed.
     */
    fun deleteProductFromCart(productId: Int) {
        viewModelScope.launch {
            checkoutRepository.removeProductFromCart(productId)
        }
    }

    /**
     * Increases the quantity of a specific product in the shopping cart.
     *
     * @param productId The unique identifier of the product whose quantity should be increased.
     */
    fun increaseQuantityOfCartItem(productId: Int) {
        viewModelScope.launch {
            checkoutRepository.increaseQuantityOfCartItem(productId)
        }
    }


    /**
     * Removes all items currently present in the shopping cart.
     *
     */
    fun clearShoppingCart() {
        viewModelScope.launch {
            checkoutRepository.clearShoppingCart()

        }

    }

}