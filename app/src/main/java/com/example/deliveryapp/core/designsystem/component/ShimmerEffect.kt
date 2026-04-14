package com.example.deliveryapp.core.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * A composable that applies an animated shimmer highlight effect over a given area.
 * Typically used as an overlay for placeholder UI components during data loading.
 *
 * @param modifier The [Modifier] to be applied to the layout, defining its size and shape.
 * @param widthOfShadowBrush The width of the gradient highlight in pixels.
 * @param angleOfAxisY The vertical offset used to determine the angle of the linear gradient.
 * @param durationMillis The duration of one full animation cycle in milliseconds.
 */
@Composable
fun ShimmerEffect(
    modifier: Modifier,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
) {
    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.5f),
        Color.White.copy(alpha = 1.0f),
        Color.White.copy(alpha = 0.5f),
        Color.White.copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
    )

    Box(
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(brush)
        )
    }


}

/**
 * A placeholder component that mimics the layout of ProductItem
 * using the [ShimmerEffect].
 */
@Composable
fun ProductItemShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Image Placeholder
        ShimmerEffect(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Color.LightGray,
                    RoundedCornerShape(12.dp)
                )
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title Placeholder
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
                    .background(
                        Color.LightGray,
                        RoundedCornerShape(4.dp)
                    )
            )
            // Description Placeholder
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .background(
                        Color.LightGray,
                        RoundedCornerShape(4.dp)
                    )
            )
            Spacer(modifier = Modifier.weight(1f))
            // Price Placeholder
            ShimmerEffect(
                modifier = Modifier
                    .width(60.dp)
                    .height(24.dp)
                    .background(
                        Color.LightGray,
                        RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

/**
 * A placeholder component that mimics the layout of a recipe item
 * using the [ShimmerEffect].
 */
@Composable
fun RecipeItemShimmer() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Main Image Shimmer
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray, RoundedCornerShape(12.dp))
        )
        // Title Shimmer
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(24.dp)
                .background(Color.LightGray, RoundedCornerShape(4.dp))
        )
        // Description/Time Shimmer
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(16.dp)
                .background(Color.LightGray, RoundedCornerShape(4.dp))
        )
    }
}