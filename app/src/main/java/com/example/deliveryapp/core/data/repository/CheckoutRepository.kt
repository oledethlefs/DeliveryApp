package com.example.deliveryapp.core.data.repository

import android.util.Log
import com.example.deliveryapp.core.data.model.CartProduct
import com.example.deliveryapp.core.datastore.CheckoutDataSource
import com.example.deliveryapp.core.network.NetworkDataSource
import com.example.deliveryapp.core.network.model.asModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Repository responsible for managing shopping cart operations and checkout data.
 *
 * @property checkoutDataSource The data source for managing the shopping cart.
 * @property network The network data source for fetching product details.
 *
 */
class CheckoutRepository @Inject constructor(
    private val checkoutDataSource: CheckoutDataSource,
    private val network: NetworkDataSource
)  {

    /**
     * A stream of products currently in the shopping cart, including their amount.
     *
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val productsInCart: Flow<List<CartProduct>> =
        checkoutDataSource.productsInShoppingCart.flatMapLatest { productIds ->

            flow {
                if (productIds.isEmpty()) {
                    emit(emptyList())
                    return@flow
                }
                // 1. Count occurrences of each product ID in saved cart
                val productCounts = productIds.groupingBy { it }.eachCount()

                // 2. Fetch unique product details from the network
                val uniqueProductIds = productCounts.keys.toList()
                val networkProducts = try {
                    network.getProducts(uniqueProductIds)
                } catch (e: Exception) {
                    Log.e("CheckoutRepository", "Error fetching products: ${e.message}")
                    emptyList()
                }




                // 3. Map network products to CartProduct, including the count
                val cartProducts = networkProducts.mapNotNull { networkProduct ->
                    val count = productCounts[networkProduct.id]
                    if (count != null) {
                        CartProduct(product = networkProduct.asModel(), countInCart = count)
                    } else {
                        null
                    }
                }
                emit(cartProducts)
            }
        }

    /**
     * A [Flow] that provides the current service fee calculated based on the items
     * in the shopping cart.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val serviceFee: Flow<Int> =
        checkoutDataSource.productsInShoppingCart.flatMapLatest { productIds ->
            flow {
                if (productIds.isEmpty()) {
                    emit(0)
                    return@flow
                }
                val serviceFee = try {
                    network.getServiceFee(productIds)
                } catch (e: Exception) {
                    Log.e("CheckoutRepository", "Error fetching service fee: ${e.message}")
                }
                emit( serviceFee)
            }
        }



    /**
     * Adds a list of products to the shopping cart.
     *
     * @param products A list of product identifiers to be added to the cart.
     */
    suspend fun addProductsToCart(products: List<Int>) {
        checkoutDataSource.addProductsToCart(products)
    }



    /**
     * Clears all products currently stored in the shopping cart.
     */
    suspend fun clearShoppingCart() {
        checkoutDataSource.clearShoppingCart()
    }

    /**
     * Decreases the quantity of a specific product in the shopping cart.
     *
     * @param productId The unique identifier of the product whose quantity should be decreased.
     */
    suspend fun decreaseQuantityOfCartItem(productId: Int) {
        checkoutDataSource.decreaseQuantityOfCartItem(productId)
    }

    /**
     * Increases the quantity of a specific product in the shopping cart.
     *
     * @param productId The unique identifier of the product to increment.
     */
    suspend fun increaseQuantityOfCartItem(productId: Int) {
        checkoutDataSource.increaseQuantityOfCartItem(productId)

    }

    /**
     * Removes all instances of a specific product from the shopping cart.
     *
     * @param productId The unique identifier of the product to be removed.
     */
    suspend fun removeProductFromCart(productId: Int) {
        checkoutDataSource.removeProductFromCart(productId)
    }


    /**
     * Fetches all products associated with a specific recipe and adds them to the shopping cart.
     *
     * This method retrieves the list of products required for the given [recipeId] from the
     * network and persists their IDs into the cart data source.
     *
     * @param recipeId The unique identifier of the recipe to add products from.
     */
    suspend fun addProductsFromRecipeToCart(recipeId: Int) {
        try {
            val products = network.getProductsFromRecipe(recipeId)
            val productIds = products.map { it.id }
            checkoutDataSource.addProductsToCart(productIds)
        }
        catch (e: Exception) {
            Log.e("CheckoutRepository", "Error fetching products from recipe: ${e.message}")
        }

    }

    /**
     * Fetches all products associated with a specific recipe and deletes them from the shopping cart.
     *
     * @param recipeId The unique identifier of the recipe to add products from.
     */
    suspend fun deleteProductsFromRecipeFromCart(recipeId: Int) {
        try {
            val products = network.getProductsFromRecipe(recipeId)
            val productIds = products.map { it.id }
            checkoutDataSource.deleteProductsFromCart(productIds)
        }
        catch (e: Exception) {
            Log.e("CheckoutRepository", "Error fetching products from recipe: ${e.message}")
        }
    }



}