package com.example.deliveryapp.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Data source responsible for managing and persisting shopping cart information.
 *
 * This class provides an abstraction over [DataStore] to handle operations related to the
 * user's checkout process, such as adding products, modifying item quantities, and
 * clearing the shopping cart.
 *
 * @property checkoutData The [DataStore] instance used to persist [UserPreferences].
 */
class CheckoutDataSource @Inject constructor(
    private val checkoutData: DataStore<UserPreferences>
) {

    /**
     * A [Flow] representing the list of product IDs currently stored in the user's shopping cart.
     *
     * This stream emits a updated list of IDs whenever the cart contents change within the [DataStore].
     */
    val productsInShoppingCart: Flow<List<Int>> = checkoutData.data
        .map { data ->
            data.productsInShoppingCartList
        }

    /**
     * Adds a list of product IDs to the shopping cart.
     *
     * @param productIdsToAdd The list of product IDs to be added to the current shopping cart.
     */
    suspend fun addProductsToCart(productIdsToAdd: List<Int>) {
        checkoutData.updateData { currentProducts ->
            currentProducts.toBuilder()
                .addAllProductsInShoppingCart(productIdsToAdd)
                .build()
        }

    }

    /**
     * Removes a list of product IDs from the shopping cart.
     *
     *
     * @param productIdsToDelete The list of product IDs to be added to the current shopping cart.
     */
    suspend fun deleteProductsFromCart(productIdsToDelete: List<Int>) {
        checkoutData.updateData { currentProducts ->
            val currentIds = currentProducts.productsInShoppingCartList.toMutableList()
            productIdsToDelete.forEach { idToRemove ->
                currentIds.remove(idToRemove)
            }
            currentProducts.toBuilder()
                .clearProductsInShoppingCart()
                .addAllProductsInShoppingCart(currentIds)
                .build()
        }

    }

    /**
     * Increases the quantity of a specific product in the shopping cart.
     *
     * This function adds an additional instance of the provided [productId] to the
     * persisted shopping cart list.
     *
     * @param productId The unique identifier of the product whose quantity should be increased.
     */
    suspend fun increaseQuantityOfCartItem(productId: Int) {
        checkoutData.updateData { currentProducts ->
            val currentIds = currentProducts.productsInShoppingCartList.toMutableList()
            currentIds.add(productId)
            currentProducts.toBuilder()
                .clearProductsInShoppingCart()
                .addAllProductsInShoppingCart(currentIds)
                .build()
        }

    }

    /**
     * Clears all products from the shopping cart.
     *
     * This method updates the underlying [DataStore] to remove all existing product IDs,
     * effectively emptying the user's current selection.
     */
    suspend fun clearShoppingCart() {
        checkoutData.updateData { currentProducts ->
            currentProducts.toBuilder()
                .clearProductsInShoppingCart()
                .build()
        }

    }

    /**
     * Decreases the quantity of a specific product in the shopping cart by one.
     *
     * This method removes a single occurrence of the provided [productId] from the
     * persisted list of products in the data store.
     *
     * @param productId The unique identifier of the product whose quantity should be decreased.
     */
    suspend fun decreaseQuantityOfCartItem(productId: Int) {
        checkoutData.updateData { currentProducts ->
            val currentIds = currentProducts.productsInShoppingCartList.toMutableList()
            currentIds.remove(productId)
            currentProducts.toBuilder()
                .clearProductsInShoppingCart()
                .addAllProductsInShoppingCart(currentIds)
                .build()
        }

    }

    /**
     * Removes all occurrences of a specific product from the shopping cart.
     *
     * @param productId The unique identifier of the product to be removed.
     */
    suspend fun removeProductFromCart(productId: Int) {
        checkoutData.updateData { currentProducts ->
            val currentIds = currentProducts.productsInShoppingCartList.toMutableList()
            currentIds.removeAll { it == productId }
            currentProducts.toBuilder()
                .clearProductsInShoppingCart()
                .addAllProductsInShoppingCart(currentIds)
                .build()
        }

    }


}