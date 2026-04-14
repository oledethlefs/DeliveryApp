package com.example.deliveryapp.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deliveryapp.R
import com.example.deliveryapp.core.data.model.CartProduct
import com.example.deliveryapp.core.data.model.Product
import com.example.deliveryapp.core.data.model.fritzKolaSingle033
import com.example.deliveryapp.core.designsystem.icon.Icons
import com.example.deliveryapp.core.designsystem.theme.DeliveryAppTheme


/**
 * A composable that represents a single item in the shopping cart.
 *
 * This component displays product details, including an image and price, and provides
 * controls for adjusting the quantity. It also supports a swipe-to-dismiss gesture
 *
 * @param product The [Product] object representing the item to be displayed.
 * @param productQuantity The current quantity of the item in the cart.
 * @param onProductClick The callback to be invoked when the product item is clicked.
 * @param onIncreaseQuantity The callback to be invoked when the increase quantity button is clicked.
 * @param onDecreaseQuantity The callback to be invoked when the decrease quantity button is clicked.
 * @param onDelete The callback to be invoked when the item is deleted from the cart.
 * @param modifier The [Modifier] to be applied to the layout.
 */
@Composable
fun CartItem(
    product: Product,
    isFavorite: Boolean,
    onAddToFavoritesClick: () -> Unit,
    productQuantity: Int,
    onProductClick: (Product) -> Unit,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it != SwipeToDismissBoxValue.Settled) {
                onDelete()
                true
            } else false
        },
        positionalThreshold = { totalDistance -> totalDistance * 0.5f }
    )

    val updatedProductWithQuantity = remember(product, productQuantity) {
        product.copy(
            price = product.price * productQuantity,
            reducedPrice = product.reducedPrice * productQuantity
        )
    }

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        modifier = modifier,
        backgroundContent = {
            val alignment = when (swipeToDismissBoxState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                else -> Alignment.Center
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red.copy(alpha = 0.2f))
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    painter = painterResource(id = Icons.delete),
                    contentDescription = stringResource(id = R.string.delete_from_shopping_cart),
                    tint = Color.Red.copy(alpha = 0.8f)
                )
            }
        }
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.Top)
                ) {
                    AddProductToFavoritesButton(
                        product = product,
                        isFavorite = isFavorite
                    ) {
                        onAddToFavoritesClick()
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    ProductDiscountBadge(
                        price = product.price.toFloat(),
                        reducedPrice = product.reducedPrice.toFloat(),
                        modifier = Modifier.padding(bottom = 4.dp)



                    )
                }
                BasicProductItem(
                    product = updatedProductWithQuantity,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onProductClick(product) }
                )
                ProductQuantityStepper(
                    quantity = productQuantity,
                    onIncrement = onIncreaseQuantity,
                    onDecrement = onDecreaseQuantity
                )
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun CartItemPreview() {
    DeliveryAppTheme {
        val quantity = remember { mutableIntStateOf(1) }
        val cartProduct = CartProduct(fritzKolaSingle033, quantity.intValue)
        CartItem(product = cartProduct.product,
            isFavorite = false,
            onAddToFavoritesClick = {},
            productQuantity = cartProduct.countInCart,
            onIncreaseQuantity = {quantity.intValue++},
            onDecreaseQuantity = {quantity.intValue--},
            onDelete = {},
            onProductClick = {})
    }
}
