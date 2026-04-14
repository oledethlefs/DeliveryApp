package com.example.deliveryapp.core.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.example.deliveryapp.R
import com.example.deliveryapp.core.data.model.CartProduct
import com.example.deliveryapp.core.data.model.Product
import com.example.deliveryapp.core.data.model.ProductCategory
import com.example.deliveryapp.core.data.model.fritzKolaBox033
import com.example.deliveryapp.core.data.model.fritzKolaBox05
import com.example.deliveryapp.core.data.model.fritzKolaSingle033
import com.example.deliveryapp.core.data.model.fritzKolaSingle05
import com.example.deliveryapp.core.designsystem.icon.Icons
import com.example.deliveryapp.core.designsystem.theme.DeliveryAppTheme


/**
 * A basic building block for displaying product information in a row layout.
 *
 * This component displays the product image alongside its primary details, including
 * the brand, name, quantity, and pricing. It handles the display of discounted prices
 * by showing the original price with a strikethrough if applicable.
 *
 * @param product The [Product] data model containing information to display.
 * @param modifier The [Modifier] to be applied to the row layout.
 * @param productImageSize The size ([Dp]) of the product image. Defaults to 125.dp.
 * @param disableName Whether to disable the product name.
 * @param disableBrand Whether to disable the product brand.
 */
@Composable
fun BasicProductItem(
    product: Product,
    modifier: Modifier = Modifier,
    productImageSize: Dp = 125.dp,
    disableName: Boolean = false,
    disableBrand: Boolean = false

    ) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = stringResource(R.string.product_image),
                modifier = Modifier.size(productImageSize),
            )


        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!disableBrand)
                Text(
                text = product.brand,
                style = MaterialTheme.typography.labelSmall,
                )
            if (!disableName)
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )

            Text(
                text = product.quantity,
                style = MaterialTheme.typography.labelSmall,
            )
            Spacer(modifier = Modifier.height(16.dp))

            // if on discount, show old price
            if (product.reducedPrice != product.price) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "(${formatPrice(product.price)})",
                        style = MaterialTheme.typography.labelSmall.copy(textDecoration = TextDecoration.LineThrough),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ProductDiscountBadge(
                        price = product.price.toFloat(),
                        reducedPrice = product.reducedPrice.toFloat(),
                    )
                }


            }

            Text(
                text = formatPrice(product.reducedPrice),
                color = if (product.reducedPrice != product.price) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = product.quantityPrice,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}


/**
 * A composable extending the [BasicProductItem] by adding a favorite button, quantity stepper,
 * and an add to cart button.
 *
 * @param product The [Product] data to display.
 * @param favoriteProductsIds The list of product IDs that are currently marked as favorites.
 * @param cartItems The list of [CartProduct].
 * @param onProductClick Callback invoked when the main product area is clicked.
 * @param onIncrementQuantity Callback invoked when the user wants to add one unit to the cart.
 * @param onDecrementQuantity Callback invoked when the user wants to remove one unit from the cart.
 * @param onAddToFavoritesClick Callback invoked when the favorite button is toggled.
 * @param modifier The [Modifier] to be applied to the row layout.
 * @param disableExpand Whether to disable the "other quantities" section.
 * @param productImageSize The size ([Dp]) of the product image. Defaults to 125.dp.
 * @param disableName Whether to disable the product name.
 * @param disableBrand Whether to disable the product brand.
 */
@Composable
fun ProductItem(
    product: Product,
    favoriteProductsIds: List<Int>,
    cartItems: List<CartProduct>,
    onProductClick: (Product) -> Unit,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
    onAddToFavoritesClick: (Product) -> Unit,
    modifier: Modifier = Modifier,
    disableExpand: Boolean = false,
    productImageSize: Dp = 125.dp,
    disableName: Boolean = false,
    disableBrand: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    val cartItem = cartItems.find { it.product.id == product.id }
    val quantity = cartItem?.countInCart ?: 0
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
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
                    isFavorite = product.id in favoriteProductsIds
                ) {
                    onAddToFavoritesClick(product)
                }
                if (product.otherQuantities.isNotEmpty() && !disableExpand) {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            painter = painterResource(id = Icons.expand),
                            contentDescription = stringResource(id = R.string.other_quantities),
                            modifier = Modifier.rotate(if (expanded) 180f else 0f)
                        )
                    }
                }
            }
            BasicProductItem(
                product = product,
                productImageSize = productImageSize,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onProductClick(product) },
                disableName = disableName,
                disableBrand = disableBrand
            )

            AnimatedContent(
                targetState = quantity > 0,
                transitionSpec = {
                    if (targetState) {
                        // Transition from Button to Stepper
                        (fadeIn(animationSpec = tween(200, delayMillis = 90)) +
                                scaleIn(
                                    initialScale = 0.8f,
                                    animationSpec = tween(200, delayMillis = 90)
                                ))
                            .togetherWith(fadeOut(animationSpec = tween(90)))
                    } else {
                        // Transition from Stepper to Button
                        (fadeIn(animationSpec = tween(200, delayMillis = 90)) +
                                scaleIn(
                                    initialScale = 0.8f,
                                    animationSpec = tween(200, delayMillis = 90)
                                ))
                            .togetherWith(fadeOut(animationSpec = tween(90)))
                    }
                },
                label = "ButtonToStepperTransition"
            ) { isInCart ->
                if (isInCart) {
                    ProductQuantityStepper(
                        quantity = quantity,
                        onIncrement = onIncrementQuantity,
                        onDecrement = onDecrementQuantity
                    )
                } else {
                    AddToShoppingCartButton(product) { onIncrementQuantity() }
                }
            }

        }
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, top = 36.dp),
                verticalArrangement = Arrangement.spacedBy(36.dp)
            ) {
                product.otherQuantities.forEach { otherProduct ->
                    ProductItem(
                        disableExpand = true,
                        disableName = true,
                        disableBrand = true,
                        productImageSize = 75.dp,
                        product = otherProduct,
                        favoriteProductsIds = favoriteProductsIds,
                        cartItems = cartItems,
                        onProductClick = onProductClick,
                        onIncrementQuantity = onIncrementQuantity,
                        onDecrementQuantity = onDecrementQuantity,
                        onAddToFavoritesClick = onAddToFavoritesClick
                    )
                }
            }
        }
    }

}

/**
 * A detailed card component that displays comprehensive information about a product.
 *
 * This component features a prominent product image with a shimmer loading effect,
 * brand and name details, pricing (including discount handling), Nutri-Score indicator,
 * and a favorites toggle. It also includes an animated transition between an "Add to Cart"
 * button and a quantity stepper based on the current cart status.
 *
 * @param product The [Product] data model containing all display information.
 * @param isFavorite Boolean indicating if the product is currently marked as a favorite.
 * @param onAddToFavoritesClick Callback invoked when the favorite button is toggled.
 * @param onIncrementQuantity Callback invoked to increase the quantity of the product in the cart.
 * @param onDecrementQuantity Callback invoked to decrease the quantity of the product in the cart.
 * @param quantityInCart The current number of units of this product in the shopping cart.
 * @param modifier The [Modifier] to be applied to the card container.
 */
@Composable
fun ProductCard(
    product: Product,
    isFavorite: Boolean,
    onAddToFavoritesClick: () -> Unit,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
    quantityInCart: Int,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
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
            SubcomposeAsyncImage(
                model = product.imageUrl,
                contentDescription = stringResource(R.string.product_image),
                modifier = Modifier.size(150.dp),
                loading = {
                    // This shows while the image is fetching
                    ShimmerEffect(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Color.LightGray,
                                RoundedCornerShape(8.dp)
                            )
                    )
                }
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {

                Text(
                    text = product.brand,
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = product.quantity,
                    style = MaterialTheme.typography.labelSmall,
                )

                Spacer(modifier = Modifier.height(16.dp))


                // if on discount, show old price
                if (product.reducedPrice != product.price) {

                    Text(
                        text = "(${formatPrice(product.price)})",
                        style = MaterialTheme.typography.labelSmall.copy(textDecoration = TextDecoration.LineThrough),
                    )
                }
                Text(
                    text = formatPrice(product.reducedPrice),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = product.quantityPrice,
                    style = MaterialTheme.typography.labelSmall,
                )

                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    model = "android.resource://com.example.deliveryapp/" + when (product.nutriScore) {
                        "A" -> R.drawable.nutri_a
                        "B" -> R.drawable.nutri_b
                        "C" -> R.drawable.nutri_c
                        "D" -> R.drawable.nutri_d
                        else ->
                            R.drawable.nutri_e
                    },
                    contentDescription = stringResource(R.string.nutri_score),
                    modifier = Modifier.width(80.dp),
                )
            }
            AnimatedContent(
                modifier = Modifier.padding(end=8.dp),
                targetState = quantityInCart > 0,
                transitionSpec = {
                    if (targetState) {
                        // Transition from Button to Stepper
                        (fadeIn(animationSpec = tween(200, delayMillis = 90)) +
                                scaleIn(
                                    initialScale = 0.8f,
                                    animationSpec = tween(200, delayMillis = 90)
                                ))
                            .togetherWith(fadeOut(animationSpec = tween(90)))
                    } else {
                        // Transition from Stepper to Button
                        (fadeIn(animationSpec = tween(200, delayMillis = 90)) +
                                scaleIn(
                                    initialScale = 0.8f,
                                    animationSpec = tween(200, delayMillis = 90)
                                ))
                            .togetherWith(fadeOut(animationSpec = tween(90)))
                    }
                },
                label = "ButtonToStepperTransition"
            ) { isInCart ->
                if (isInCart) {
                    ProductQuantityStepper(
                        quantity = quantityInCart,
                        onIncrement = onIncrementQuantity,
                        onDecrement = onDecrementQuantity
                    )
                } else {
                    AddToShoppingCartButton(product) { onIncrementQuantity() }
                }
            }

        }

    }


}


/**
 * A component that displays a section for the product's description.
 *
 * @param description The detailed text description of the product.
 * @param modifier The [Modifier] to be applied to the column layout.
 */
@Composable
fun ProductDescription(
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.product_description),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * A composable that displays the list of ingredients for a product.
 *
 * @param ingredients The string containing the list of ingredients to be displayed.
 * @param modifier The [Modifier] to be applied to the column layout.
 */
@Composable
fun ProductIngredients(
    ingredients: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.ingredients),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = ingredients,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


/**
 * A composable that displays a specific warning or additional information related to a product.
 *
 * @param warning The text string containing the warning or information to be displayed.
 * @param modifier The [Modifier] to be applied to the column layout.
 */
@Composable
fun ProductWarning(
    warning: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.product_warning),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = warning,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * A card-based component that displays a product category.
 *
 * @param category The [ProductCategory] data model containing the name and image URL.
 * @param modifier The [Modifier] to be applied to the card layout.
 * @param onCategoryClick Callback invoked when the category card is clicked.
 */
@Composable
fun ProductCategory(
    category: ProductCategory,
    modifier: Modifier = Modifier,
    onCategoryClick: (ProductCategory) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCategoryClick(category) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = stringResource(R.string.product_category),
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(120.dp),
            )
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(12.dp),
                textAlign = TextAlign.Center
            )
        }

    }
}


/**
 * Displays a section containing alternative quantities or sizes for a product, if available.
 *
 * @param otherQuantities The list of alternative quantities or sizes.
 * @param cartItems The list of cart items.
 * @param onIncrementQuantity Callback invoked when the user wants to add one unit to the cart.
 * @param onDecrementQuantity Callback invoked when the user wants to remove one unit from the cart.
 * @param favoriteProductsIds The list of product IDs that are currently marked as favorites.
 * @param setProductFavorite Callback invoked when the favorite button is toggled.
 * @param modifier The [Modifier] to be applied to the column layout.
 * @param onProductClick Callback invoked when a product item is clicked.
 */
@Composable
fun OtherQuantitiesOfProduct(
    otherQuantities: List<Product>,
    cartItems: List<CartProduct>,
    onIncrementQuantity: (Int) -> Unit,
    onDecrementQuantity: (Int) -> Unit,
    favoriteProductsIds: List<Int>,
    setProductFavorite: (Product) -> Unit,
    modifier: Modifier = Modifier,
    onProductClick: (Product) -> Unit = {},
) {
    // Only show if there are other quantities available
    if (otherQuantities.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.other_quantities),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                otherQuantities.forEachIndexed { index, product ->
                    if (index > 0) HorizontalDivider()
                    ProductItem(
                        disableExpand = true,
                        product = product,
                        cartItems = cartItems,
                        favoriteProductsIds = favoriteProductsIds,
                        onAddToFavoritesClick = { setProductFavorite(product) },
                        onIncrementQuantity = { onIncrementQuantity(product.id) },
                        onDecrementQuantity = { onDecrementQuantity(product.id) },
                        onProductClick = onProductClick,
                    )
                }
            }

        }
    }
}

/**
 * A comprehensive detail view for a single product, displayed in a scrollable column.
 *
 * This component aggregates several subcomponents to provide a full overview of a product,
 * including a featured [ProductCard], detailed description, nutritional information,
 * ingredients, warnings, and a list of other available quantities or variations.
 *
 * @param product The main [Product] entity to display.
 * @param cartItems The current list of [CartProduct]s to determine the quantity of this and related products in the cart.
 * @param isFavorite Whether the main product is currently marked as a favorite.
 * @param favoriteProductsIds A list of IDs for all products currently marked as favorites, used for the "other quantities" section.
 * @param setProductFavorite Callback invoked when the favorite status of a product is toggled.
 * @param onIncrementQuantity Callback invoked with a product ID when the user wants to add one unit to the cart.
 * @param onDecrementQuantity Callback invoked with a product ID when the user wants to remove one unit from the cart.
 * @param modifier The [Modifier] to be applied to the scrollable column.
 */
@Composable
fun ProductDetail(
    product: Product,
    cartItems: List<CartProduct>,
    isFavorite: Boolean,
    favoriteProductsIds: List<Int>,
    setProductFavorite: (Product) -> Unit,
    onIncrementQuantity: (Int) -> Unit,
    onDecrementQuantity: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(48.dp)
    ) {
        ProductCard(
            product = product,
            modifier = Modifier.padding(top = 12.dp),
            isFavorite = isFavorite,
            onAddToFavoritesClick = { setProductFavorite(product) },
            onIncrementQuantity = { onIncrementQuantity(product.id) },
            onDecrementQuantity = { onDecrementQuantity(product.id) },
            quantityInCart = cartItems.find { it.product.id == product.id }?.countInCart ?: 0
        )
        ProductDescription(description = product.description)
        ProductNutritionalInformation(product = product)
        ProductIngredients(ingredients = product.ingredients)
        ProductWarning(warning = product.information)
        OtherQuantitiesOfProduct(
            otherQuantities = product.otherQuantities,
            cartItems = cartItems,
            onIncrementQuantity = onIncrementQuantity,
            onDecrementQuantity = onDecrementQuantity,
            favoriteProductsIds = favoriteProductsIds,
            setProductFavorite = setProductFavorite,

            )
        Spacer(modifier = Modifier.height(48.dp)) // as FAB space
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, device = "id:pixel_7")
@Composable
fun ProductDetailPreview() {
    DeliveryAppTheme {
        Scaffold {
            val isFavorite = remember { mutableStateOf(false) }
            ProductDetail(
                product = fritzKolaSingle033,
                isFavorite = isFavorite.value,
                setProductFavorite = { isFavorite.value = !isFavorite.value },
                onIncrementQuantity = {},
                onDecrementQuantity = {},
                favoriteProductsIds = listOf(),
                cartItems = listOf(CartProduct(fritzKolaSingle05, 1)),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .consumeWindowInsets(it)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    )

            )

        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, device = "id:pixel_7")
@Composable
fun ProductItemPreview() {
    DeliveryAppTheme {
        val cartItems: List<CartProduct> = listOf()
        val favoriteProductsIds = listOf<Int>()
        var quantityInCart by remember { mutableIntStateOf(0) }
        val productExample = fritzKolaSingle05.copy(otherQuantities = listOf(fritzKolaSingle033, fritzKolaBox033, fritzKolaBox05))
        ProductItem(
            product = productExample,
            favoriteProductsIds = favoriteProductsIds,
            cartItems = cartItems,
            onProductClick = {},
            onIncrementQuantity = { quantityInCart++ },
            onDecrementQuantity = { quantityInCart-- },
            onAddToFavoritesClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, device = "id:pixel_7")
@Composable
fun ProductItemWithQuantityPreview() {
    DeliveryAppTheme {
        val cartItems = listOf(CartProduct(fritzKolaBox05, 1))
        val favoriteProductsIds = listOf<Int>()
        val productExample = fritzKolaBox05.copy(otherQuantities = listOf(fritzKolaSingle033,  fritzKolaSingle05, fritzKolaBox033))
        ProductItem(
            product = productExample,
            favoriteProductsIds = favoriteProductsIds,
            cartItems = cartItems,
            onProductClick = {},
            onIncrementQuantity = {},
            onDecrementQuantity = {},
            onAddToFavoritesClick = {},
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun OtherQuantitiesOfProductPreview() {
    DeliveryAppTheme {
        OtherQuantitiesOfProduct(
            otherQuantities = fritzKolaSingle033.otherQuantities,
            cartItems = listOf(CartProduct(fritzKolaSingle05, 1)),
            onIncrementQuantity = {},
            onDecrementQuantity = {},
            favoriteProductsIds = listOf(),
            setProductFavorite = {},

            )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ProductCategoryPreview() {
    DeliveryAppTheme {
        val category = ProductCategory(
            id = 0,
            name = "Brot, Nudeln und andere Backwaren",
            imageUrl = "android.resource://com.example.deliveryapp/" + R.drawable.carbs_category,
            childrenProductCategories = listOf(),
            productFilters = listOf()
        )
        ProductCategory(category = category)
    }
}



