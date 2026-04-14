package com.example.deliveryapp.core.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.deliveryapp.navigation.TopLevelDestination

/**
 * A composable that displays a checkout icon with an animated notification badge.
 *
 * The badge indicates the number of items currently in the cart. It automatically
 * animates its visibility based on whether the count is greater than zero and
 * uses a vertical sliding animation when the count value changes.
 *
 * @param cartItemCount The number of items in the cart to be displayed in the badge.
 */
@Composable
fun CheckoutBadgedBox(
    cartItemCount: Int,
) {

    BadgedBox(
        badge = {
            AnimatedVisibility(
                visible = cartItemCount > 0,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                if (cartItemCount > 0) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.surface
                    ) {
                        AnimatedContent(
                            targetState = cartItemCount,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    (slideInVertically { height -> height } + fadeIn()) togetherWith
                                            (slideOutVertically { height -> -height } + fadeOut())
                                } else {
                                    (slideInVertically { height -> -height } + fadeIn()) togetherWith
                                            (slideOutVertically { height -> height } + fadeOut())
                                }.using(
                                    sizeTransform = null
                                )
                            },
                            label = "CartBadgeAnimation"
                        ) { count ->
                            Text(count.toString())
                        }

                    }
                }
            }
        }
    ) {
        Icon(
            painter = painterResource(
                TopLevelDestination.CHECKOUT.unselectedIcon
            ),
            contentDescription = stringResource(TopLevelDestination.CHECKOUT.contentDescription)
        )
    }
}


/**
 * A composable that displays a badge indicating a discount on a product.
 *
 * Typically used to highlight price reductions or special offers on product cards.
 *
 * @param price The original price of the product.
 * @param reducedPrice The discounted price of the product.
 * @param modifier The [Modifier] to be applied to the badge.
 */
@Composable
fun ProductDiscountBadge(
    price: Float,
    reducedPrice: Float,
    modifier: Modifier = Modifier
) {

    val discountPercent = if (price > 0) {
        ((price - reducedPrice) / price * 100).toInt()
    } else 0

    if (discountPercent > 0) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 4.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "-$discountPercent%",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}