package com.example.deliveryapp.core.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deliveryapp.R
import com.example.deliveryapp.core.data.model.Product
import com.example.deliveryapp.core.designsystem.icon.Icons
import com.example.deliveryapp.core.designsystem.theme.DeliveryAppTheme
import kotlinx.coroutines.delay
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import com.example.deliveryapp.core.data.model.fritzKolaSingle033
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * A Floating Action Button (FAB) that allows users to add items to their shopping cart.
 *
 * This component features a scale animation and haptic feedback when clicked to provide
 * visual and tactile confirmation to the user.
 *
 * @param onClick The callback to be invoked when the button is pressed.
 */
@Composable
fun AddToShoppingCartFAB(onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current

    //Animation
    var animationTrigger by remember { mutableStateOf(false) }
    val iconScale by animateFloatAsState(
        targetValue = if (animationTrigger) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 150),
        label = "fabIconScale"
    )
    LaunchedEffect(animationTrigger) {
        if (animationTrigger) {
            delay(150)
            animationTrigger = false
        }
    }

    FloatingActionButton(
        onClick = {
            onClick()
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            animationTrigger = true
                  },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            painter = painterResource(id = Icons.add_shopping_cart),
            contentDescription = stringResource(id = R.string.add_to_shopping_cart),
            modifier = Modifier.scale(iconScale),
        )
    }
}
/**
 * A button that allows users to toggle the favorite status of a specific product.
 *
 * This component displays a heart icon that changes its appearance and color based on the
 * [isFavorite] state. It includes a scale animation and provides haptic feedback along with
 * a Toast message to confirm the action to the user.
 *
 * @param product The [Product] to be added to or removed from favorites.
 * @param isFavorite A boolean indicating whether the product is currently marked as a favorite.
 * @param onClick The callback to be invoked when the button is pressed.
 */
@Composable
fun AddProductToFavoritesButton(
    product: Product,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current

    // Adding product to favorites animation
    var animationTrigger by remember { mutableStateOf(false) }
    val iconScale by animateFloatAsState(
        targetValue = if (animationTrigger) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 150),
        label = "favoriteIconScale"
    )
    val iconTint by animateColorAsState(
        targetValue = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(durationMillis = 300), // Slower transition for color
        label = "favoriteIconTint"
    )
    LaunchedEffect(animationTrigger) {
        if (animationTrigger) {
            delay(150)
            animationTrigger = false
        }
    }
    IconButton(
        onClick = {
            onClick()
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            animationTrigger = true},
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier
    ) {
        Icon(
            painter = if (!isFavorite) painterResource(id = Icons.favorite) else painterResource(
                id = Icons.favorite_filled
            ),
            contentDescription = if (!isFavorite) stringResource(id = R.string.set_favorite) else stringResource(
                id = R.string.remove_favorite
            ),
            tint = iconTint,
            modifier = Modifier.scale(iconScale)
        )
    }
}

/**
 * A primary action button that navigates the user to the main shopping or product discovery area.
 *
 * This button is styled with the application's primary color and features rounded corners
 * with standard horizontal and vertical padding.
 *
 * @param modifier The [Modifier] to be applied to the button.
 * @param onClick The callback to be invoked when the button is pressed.
 */
@Composable
fun StartShoppingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.start_shopping),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * A button that allows users to resume their shopping experience.
 *
 * This component is styled as a [TextButton] with the application's primary color
 * scheme and rounded corners. It is typically used to navigate the user back to
 * a product catalog or a previous shopping state.
 *
 * @param modifier The [Modifier] to be applied to the button.
 * @param onClick The callback to be invoked when the button is clicked.
 */
@Composable
fun ResumeShoppingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.resume_shopping),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * A button used to navigate to the order details or order history screen.
 *
 * This component is styled as a [TextButton] with a primary color background,
 * rounded corners, and standard padding. It is typically used as a call-to-action
 * for users to view the status or summary of an existing order.
 *
 * @param modifier The [Modifier] to be applied to the button.
 * @param onClick The callback to be invoked when the button is clicked.
 */
@Composable
fun LookAtOrderButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.look_at_your_order),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * A full-width text button labeled "Done" used to confirm or finalize an action.
 *
 * This button uses the primary color scheme of the application and features
 * rounded corners and standard vertical padding.
 *
 * @param modifier The [Modifier] to be applied to the button.
 * @param onClick The callback to be invoked when the button is clicked.
 */
@Composable
fun DoneButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.done),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * A button used to reset filters or input fields to their default state.
 *
 * This component is styled as a [TextButton] with a high-contrast color scheme
 * (onPrimary container and primary content) to distinguish it from primary actions.
 *
 * @param modifier The [Modifier] to be applied to the button.
 * @param onClick The callback to be invoked when the button is clicked.
 */
@Composable
fun ResetButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.reset),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * A square button component used to add a specific [Product] to the shopping cart.
 *
 * This button features:
 * - A scale animation on the icon when clicked.
 * - Haptic feedback (long press) upon interaction.
 * - A Toast message confirming the addition of the product.
 * - A primary color background with a rounded corner shape.
 *
 * @param product The [Product] model representing the item to be added.
 * @param onClick The callback to be executed when the button is clicked.
 */
@Composable
fun AddToShoppingCartButton(
    product: Product,
    onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                onClick()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = Icons.add_shopping_cart),
            contentDescription = stringResource(id = R.string.add_to_shopping_cart),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }

}

/**
 * An interactive slider button used to confirm a purchase.
 *
 * This component requires the user to swipe a thumb from left to right to trigger an action.
 * It features several visual states:
 * - **Initial/Idle:** Shows a "Slide to purchase" hint and a wobbling thumb icon.
 * - **Swiping:** The background color transitions from the primary color to a neutral color,
 *   and the hint text fades out.
 * - **Processing:** Once swiped, a [CircularProgressIndicator] appears inside the thumb
 *   and the text changes to "Processing payment".
 * - **Completed:** Triggered by the [isCompleted] flag, the button turns green, shows
 *   a "Payment successful" message, and displays a checkmark icon.
 *
 * @param isCompleted A boolean flag indicating if the purchase process has finished successfully.
 * @param onPurchase The callback to be invoked when the user successfully swipes the button to the end.
 * @param modifier The [Modifier] to be applied to the button's outer container.
 * @param paddings The internal padding between the track and the sliding thumb.
 * @param thumbSize The size of the circular draggable thumb.
 * @param trackShape The shape of the button's background track.
 * @param fractionalThreshold The percentage of the track width (0.0 to 1.0) that must be swiped
 * to trigger the [onPurchase] callback.
 */
@Composable
fun SlideToPurchaseButton(
    isCompleted: Boolean,
    onPurchase: () -> Unit,
    modifier: Modifier = Modifier,
    paddings: PaddingValues = PaddingValues(4.dp),
    thumbSize: DpSize = DpSize(36.dp, 36.dp),
    trackShape: Shape = RoundedCornerShape(percent = 50),
    fractionalThreshold: Float = 0.85f
) {
    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    var isSlided by remember { mutableStateOf(false) }

    var measuredWidth by remember { mutableStateOf<Float?>(null) }
    val thumbWidthPx = with(density) { thumbSize.width.toPx() }

    val anchors = remember(measuredWidth) {
        val width = measuredWidth
        if (width != null) {
            val totalPadding = with(density) {
                paddings.calculateStartPadding(layoutDirection).toPx() +
                        paddings.calculateEndPadding(layoutDirection).toPx()
            }
            DraggableAnchors {
                SwipeToPurchase.Start at 0f
                SwipeToPurchase.End at (width - totalPadding - thumbWidthPx)
            }
        } else {
            DraggableAnchors { }
        }
    }

    val draggableState = remember {
        AnchoredDraggableState(
            initialValue = SwipeToPurchase.Start,
            anchors = anchors,
            positionalThreshold = { totalDistance -> totalDistance * fractionalThreshold },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(durationMillis = 250),
            decayAnimationSpec = exponentialDecay()
        )
    }

    LaunchedEffect(anchors) {
        if (anchors.size > 0 && draggableState.anchors != anchors) {
            draggableState.updateAnchors(anchors)
        }
    }

    LaunchedEffect(isCompleted) {
        if(isCompleted) {
            draggableState.animateTo(SwipeToPurchase.End)
        }
    }

    // thumb animation
    val infiniteTransition = rememberInfiniteTransition(label = "wobbleTransition")
    val wobbleOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = CubicBezierEasing(0.8f, 0f, 0.2f, 1f)
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wobbleOffset"
    )

    val animateColor: Float by animateFloatAsState(
        targetValue = if (isCompleted) 1f else 0f,
        label = "alpha",
        animationSpec = tween(durationMillis = 700),
    )

    val slidedHintColor = Color.White
    val startTrackColor = MaterialTheme.colorScheme.primary

    val endTrackColor = lerp(Color(0xFFB4AFB4), Color(0xFF11D483), animateColor)
    val thumbIconColor = if (isCompleted) Color(0xFF11D483) else MaterialTheme.colorScheme.primary
    val thumbColor = Color.White

    val progressColor = Color(0xFF11D483)

    LaunchedEffect(draggableState.currentValue) {
        snapshotFlow { draggableState.currentValue }
            .distinctUntilChanged()
            .collect { value ->
                // When the user swipes to the end, set the internal state and call onPurchase
                if (value == SwipeToPurchase.End && !isSlided) {
                    isSlided = true
                    onPurchase()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            }
    }

    val offset by remember {
        derivedStateOf {
            if (draggableState.offset.isNaN()) 0f else draggableState.offset
        }
    }

    val slideFraction by remember {
        derivedStateOf {
            val end = draggableState.anchors.positionOf(SwipeToPurchase.End)
            val start = draggableState.anchors.positionOf(SwipeToPurchase.Start)
            if (end > start && !draggableState.offset.isNaN()) {
                ((draggableState.offset - start) / (end - start)).coerceIn(0f, 1f)
            } else {
                0f
            }
        }
    }

    val endOfColorChangeFraction = 0.85f
    val fraction = (slideFraction / endOfColorChangeFraction).coerceIn(0f..1f)
    val trackColor = lerp(startTrackColor, endTrackColor, fraction)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged {
                measuredWidth = it.width.toFloat()
            }
            .background(
                color = trackColor,
                shape = trackShape,
            )
            .padding(paddingValues = paddings)
            .anchoredDraggable(
                state = draggableState,
                orientation = Orientation.Horizontal,
                // The button is disabled once the swipe starts or is completed
                enabled = !isSlided && !isCompleted,
            ),
    ) {
        // hint
        AnimatedContent(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            targetState = isSlided || isCompleted, // Show processing/completed text based on this
            label = "HintTextAnimation"
        ) { targetState ->
            if (isCompleted) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.payment_successful),
                    textAlign = TextAlign.Center,
                    color = slidedHintColor,
                    style = MaterialTheme.typography.titleMedium,
                )
            } else if (!targetState) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = paddings.calculateStartPadding(
                                layoutDirection,
                            ),
                        ),
                    text = stringResource(id = R.string.slide_to_purchase),
                    textAlign = TextAlign.Center,
                    color = hintColor(fraction),
                    style = MaterialTheme.typography.titleMedium,
                )
            } else {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.processing_payment),
                    textAlign = TextAlign.Center,
                    color = slidedHintColor,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
        Box(
            modifier = Modifier.offset {
                IntOffset(offset.roundToInt(), 0)
            },
        ) {
            // thumb
            Box(
                modifier = Modifier
                    .size(thumbSize)
                    .background(color = thumbColor, shape = CircleShape),
            ) {
                if (isCompleted) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(thumbSize / 2),
                        painter = painterResource(id = Icons.done),
                        tint = thumbIconColor,
                        contentDescription = stringResource(id = R.string.payment_successful),
                    )
                } else if (isSlided) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(8.dp),
                        color = progressColor,
                        strokeWidth = 3.dp,
                    )
                } else {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(thumbSize / 2)
                            .offset { IntOffset(wobbleOffset.roundToInt(), 0) },
                        painter = painterResource(id = Icons.purchase),
                        tint = thumbIconColor,
                        contentDescription = stringResource(id = R.string.slide_to_purchase),
                    )
                }
            }
            }
        }
    }


/**
 * Fades out the hint color as the user progresses the swipe.
 */
@Stable
@Composable
fun hintColor(
    slideFraction: Float,
    startHintColor: Color = Color.White,
    endHintColor: Color = Color.White.copy(alpha = 0f)
): Color {
    val endOfFadeFraction = 0.45f
    val fraction = (slideFraction / endOfFadeFraction).coerceIn(0f..1f)
    return lerp(startHintColor, endHintColor, fraction)
}

private enum class SwipeToPurchase { Start, End }




/**
 * A Floating Action Button (FAB) that triggers a scroll-to-top action.
 *
 * This component provides a circular button with an upward arrow icon. When clicked,
 * it executes the provided [onClick] callback and performs a haptic feedback
 * long press effect to notify the user of the interaction.
 *
 * @param onClick The callback to be invoked when the button is pressed,
 * typically used to animate a list or scrollable container back to the top.
 */
@Composable
fun ScrollToTopButtonFAB(onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    FloatingActionButton(
        onClick = { onClick(); haptic.performHapticFeedback(HapticFeedbackType.LongPress) },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(painter = painterResource(id = Icons.arrow_up), contentDescription = stringResource(id = R.string.scroll_to_top))
    }
}

/**
 * A button that allows users to increment or decrement the number of servings.
 *
 * This button displays the current serving count and provides two [IconButton]s for
 * adjustment. It includes haptic feedback and a scale animation when the count is changed.
 *
 * @param servings A [MutableState] holding the current number of servings.
 * @param modifier The [Modifier] to be applied to the column layout.
 */
@Composable
fun ServingsButton(servings: MutableState<Int>, modifier: Modifier = Modifier) {

    val haptic = LocalHapticFeedback.current
    //Animation
    var animationTrigger by remember { mutableStateOf(false) }
    val iconScale by animateFloatAsState(
        targetValue = if (animationTrigger) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 150),
        label = "fabIconScale"
    )
    LaunchedEffect(animationTrigger) {
        if (animationTrigger) {
            delay(150)
            animationTrigger = false
        }
    }
    Column (modifier) {
        Box (modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .padding(4.dp)) {
            // Left "add" icon
            IconButton(onClick = {
                servings.value++
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                animationTrigger = true
                                 },
                modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(painter = painterResource(id = Icons.add), contentDescription = stringResource(id = R.string.add_serving), Modifier.scale(iconScale))
            }
            // Description
            Text(text = "${servings.value} ${stringResource(id = R.string.servings)}",
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge)

            // Right "remove" icon
            IconButton(onClick = {
                if (servings.value >0) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    servings.value--
                    animationTrigger = true
                }},
                enabled = servings.value > 0,
                modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(painter = painterResource(id = Icons.remove), contentDescription =stringResource(id = R.string.remove_serving), Modifier.scale(iconScale))
            }
        }
    }

}

/**
 * A back navigation button that triggers a callback and provides haptic feedback.
 *
 * @param navigateBack The callback to be invoked when the button is clicked, typically
 * used to pop the back stack or return to the previous screen.
 */
@Composable
fun NavigationButton(navigateBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    IconButton(onClick = {
        navigateBack()
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)}
    ) {
        Icon(
            painter = painterResource(id = Icons.arrow_back),
            contentDescription = stringResource(id = R.string.navigate_back)
        )
    }
}

/**
 * A stepper component used to increase or decrease the quantity of a product.
 *
 * This component displays the current quantity between two icon buttons (increment and decrement).
 * It includes scale animations and haptic feedback to provide a responsive user experience.
 *
 * @param quantity The current quantity to be displayed.
 * @param onIncrement Callback invoked when the increase button is pressed.
 * @param onDecrement Callback invoked when the decrease button is pressed.
 * @param modifier The [Modifier] to be applied to the stepper layout.
 */
@Composable
fun ProductQuantityStepper(
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val outlineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)

    // "increasing" quantity animation
    var increaseAnimationTrigger by remember { mutableStateOf(false) }
    val increaseIconScale by animateFloatAsState(
        targetValue = if (increaseAnimationTrigger) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 150),
        label = "fabIconScale"
    )
    LaunchedEffect(increaseAnimationTrigger) {
        if (increaseAnimationTrigger) {
            delay(150)
            increaseAnimationTrigger = false
        }
    }

    // decreasing quantity animation
    var decreaseAnimationTrigger by remember { mutableStateOf(false) }
    val decreaseIconScale by animateFloatAsState(
        targetValue = if (decreaseAnimationTrigger) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 150),
        label = "fabIconScale"
    )
    LaunchedEffect(decreaseAnimationTrigger) {
        if (decreaseAnimationTrigger) {
            delay(150)
            decreaseAnimationTrigger = false
        }
    }

    Column(
        modifier = modifier
            .width(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, outlineColor, RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // "Increase"
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    onIncrement()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    increaseAnimationTrigger = true
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = Icons.add),
                contentDescription = stringResource(id = R.string.increase_quantity),
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .scale(increaseIconScale)
            )
        }

        HorizontalDivider(color = outlineColor, thickness = 1.dp)

        // Quantity
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$quantity",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
            )
        }


        HorizontalDivider(color = outlineColor, thickness = 1.dp)

        // "Decrease"
        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    onDecrement()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    decreaseAnimationTrigger = true
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = Icons.remove),
                contentDescription = stringResource(id = R.string.decrease_quantity),
                tint = if (quantity > 0) MaterialTheme.colorScheme.onSurface else outlineColor,
                modifier = Modifier
                    .size(24.dp)
                    .scale(decreaseIconScale)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductQuantityStepperPreview() {
    DeliveryAppTheme {
        val quantity = remember { mutableIntStateOf(1) }
        ProductQuantityStepper(
            quantity = quantity.intValue,
            onIncrement = { quantity.intValue++ },
            onDecrement = { if (quantity.intValue > 0) quantity.intValue-- },
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Preview
@Composable
fun AddToShoppingCartFABPreview() {
    DeliveryAppTheme {
        AddToShoppingCartFAB(onClick = {})
    }
}

@Preview
@Composable
fun AddProductToFavoritesButtonPreview() {
    DeliveryAppTheme {
        val isFavorite = remember { mutableStateOf(true) }
        AddProductToFavoritesButton(fritzKolaSingle033, isFavorite.value, onClick = {})
    }
}

@Preview
@Composable
fun AddToShoppingCartButtonPreview() {
    DeliveryAppTheme {
        AddToShoppingCartButton(fritzKolaSingle033, onClick = {})
    }
}

@Preview (showBackground = true)
@Composable
fun ServingsButtonPreview() {
    DeliveryAppTheme {
        val servings = remember { mutableIntStateOf(0) }
        ServingsButton(servings = servings, modifier = Modifier.padding(16.dp))
    }
}

@Preview (showBackground = true)
@Composable
fun SlideToPurchaseButtonPreview() {
    var isPaymentCompleted by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    DeliveryAppTheme {
        SlideToPurchaseButton(isCompleted = isPaymentCompleted,
            onPurchase = {
                coroutineScope.launch {
                    delay(2000)
                    isPaymentCompleted = true
                }
            })
    }
}

