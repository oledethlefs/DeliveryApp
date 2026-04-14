package com.example.deliveryapp.core.designsystem.component


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deliveryapp.R
import com.example.deliveryapp.core.data.model.Ingredient
import com.example.deliveryapp.core.data.model.Recipe
import com.example.deliveryapp.core.designsystem.icon.Icons
import androidx.compose.material.icons.Icons as MaterialIcons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import coil3.compose.SubcomposeAsyncImage
import com.example.deliveryapp.core.designsystem.theme.Gold
import kotlinx.coroutines.delay

/**
 * A card component that displays a summary of a [Recipe], including its image,
 * name, preparation time, popularity rating, and difficulty level.
 *
 * It features a bookmark toggle with haptic feedback and animations, and provides
 * click listeners for both the card itself and the bookmark action.
 *
 * @param recipe The recipe data to display.
 * @param isBookmarked Whether the recipe is currently bookmarked by the user.
 * @param onRecipeClick Callback invoked when the recipe card is clicked.
 * @param onBookmarkClick Callback invoked when the bookmark icon is clicked.
 * @param modifier [Modifier] to be applied to the card.
 */
@Composable
fun RecipeCard(recipe: Recipe,
               isBookmarked: Boolean,
               onRecipeClick: (Recipe) -> Unit,
               onBookmarkClick: (Recipe) -> Unit,
               modifier: Modifier = Modifier) {
    val haptic = LocalHapticFeedback.current

    // Bookmark animation
    var animationTrigger by remember { mutableStateOf(false) }
    val iconScale by animateFloatAsState(
        targetValue = if (animationTrigger) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 150),
        label = "bookmarkIconScale"
    )
    val iconTint by animateColorAsState(
        targetValue = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(durationMillis = 300), // Slower transition for color
        label = "bookmarkIconTint"
    )
    LaunchedEffect(animationTrigger) {
        if (animationTrigger) {
            delay(150)
            animationTrigger = false
        }
    }
    Card (
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        onClick = { onRecipeClick(recipe) }) {
        Box {
            SubcomposeAsyncImage(
                model = recipe.imageUrl,
                contentDescription = stringResource(R.string.recipe_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp),
                loading = {
                    ShimmerEffect(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray)
                    )
                }
            )
            IconButton( // Bookmark Icon
                onClick = {
                    onBookmarkClick(recipe)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    animationTrigger = true},
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        CircleShape
                    ),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Icon(
                    painter = if (!isBookmarked) painterResource(id = Icons.bookmark) else painterResource(id = Icons.bookmark_filled),
                    contentDescription = if (!isBookmarked) stringResource(id = R.string.set_bookmark) else stringResource(id = R.string.remove_bookmark),
                    tint = iconTint,
                    modifier = Modifier.scale(iconScale)
                )
            }
        }
        Column (modifier = Modifier.padding(12.dp)) {
            Text(text = recipe.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row (verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = Icons.duration),
                    contentDescription = stringResource(id = R.string.amount_of_time)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${recipe.preparationTime} ${stringResource(id = R.string.minutes_abbreviation)}",
                    style = MaterialTheme.typography.labelSmall)

                Spacer(modifier = Modifier.weight(.33f))

                PartialFillStar(recipe.popularity)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${recipe.popularity}/5 ",
                    style = MaterialTheme.typography.labelSmall)

                Spacer(modifier = Modifier.weight(.33f))


                Text(
                    text = when (recipe.difficulty) {
                        0 -> stringResource(id = R.string.easy)
                        1 -> stringResource(
                            id = R.string.medium
                        )
                        else -> stringResource(id = R.string.hard)
                    },
                    style = MaterialTheme.typography.labelSmall
                )
            }



        }




    }
}


/**
 * A composable function that displays a list of ingredients for a recipe.
 *
 * @param ingredients The list of [Ingredient] objects to be displayed.
 * @param servings A [MutableState] of [Int] representing the current number of servings, used to scale ingredient quantities.
 */
@Composable
fun RecipeIngredientList(ingredients: List<Ingredient>,
                   servings: MutableState<Int>) {
    Column(modifier = Modifier
        .clip(RoundedCornerShape(8.dp))) {
        ingredients.forEachIndexed { index, ingredient ->
            Row (modifier = Modifier
                .background(if (index % 2 == 0) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface)
                .padding(12.dp)
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "${ingredient.quantity * servings.value} ${ingredient.unit}",
                    modifier = Modifier.weight(.40f),
                    style = MaterialTheme.typography.bodyMedium)
                Text(text = ingredient.name,
                    modifier = Modifier.weight(.60f),
                    style = MaterialTheme.typography.bodyMedium)
            }
        }


    }
}



/**
 * A component that displays a list of step-by-step preparation instructions for a recipe.
 *
 * @param instructions A list of strings where each string represents a single step in the
 * recipe preparation process.
 */
@Composable
fun InstructionsDescription(instructions: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = stringResource(id = R.string.preparation),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge)
        instructions.forEachIndexed { index, description ->
            Row  {
                CircularNumberIcon(index + 1)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = description,
                    style = MaterialTheme.typography.bodyMedium)
            }

        }

    }
}

/**
 * A comprehensive screen component that displays the full details of a [Recipe].
 *
 * It includes a [RecipeCard] header, an interactive servings selector, a detailed list
 * of ingredients that scales based on the selected servings, step-by-step preparation
 * instructions, and nutritional information.
 *
 * @param recipe The recipe data to be displayed in detail.
 * @param isBookmarked Indicates whether the current recipe is bookmarked.
 * @param onRecipeClick Callback invoked when the recipe header card is clicked.
 * @param onBookmarkClick Callback invoked when the bookmark toggle is clicked.
 * @param modifier [Modifier] to be applied to the root container.
 */
@Composable
fun RecipeDetail(recipe: Recipe, isBookmarked: Boolean, onRecipeClick: (Recipe) -> Unit, onBookmarkClick: (Recipe) -> Unit, modifier: Modifier = Modifier) {
    val servings = remember { mutableIntStateOf(recipe.servings) }
    Column(modifier = modifier
        .fillMaxSize()
        .padding(12.dp)
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(48.dp)) {
        RecipeCard(
            recipe = recipe,
            modifier = Modifier.padding(top = 12.dp),
            isBookmarked = isBookmarked,
            onRecipeClick = onRecipeClick,
            onBookmarkClick = onBookmarkClick
        )
        Text(text = stringResource(id = R.string.ingredients_for_x_servings),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge)
        ServingsButton(servings = servings)
        RecipeIngredientList(ingredients = recipe.ingredients, servings = servings)
        InstructionsDescription(instructions = recipe.preparationDescription)
        RecipeNutritionalInformation(recipe = recipe)
        Spacer(modifier = Modifier.height(16.dp))
    }
}






/**
 * A composable that displays a number centered within a circular background and a border.
 * Commonly used as a step indicator in a list of instructions.
 *
 * @param number The integer to be displayed inside the circle.
 * @param modifier [Modifier] to be applied to the icon's layout.
 * @param textStyle The [TextStyle] configuration for the number.
 * @param circleColor The background [Color] of the circle.
 * @param borderColor The [Color] of the circle's stroke/border.
 * @param borderWidth The thickness of the border in pixels.
 */
@Composable
fun CircularNumberIcon(
    number: Int,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
    circleColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.onSurface,
    borderWidth: Float = 2f
) {
    val textMeasurer = rememberTextMeasurer()
    Box(
        modifier = modifier.size(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                color = circleColor,
                center = Offset(size.width / 2, size.height / 2),
                radius = size.minDimension / 2
            )
            drawCircle(
                color = borderColor,
                center = Offset(size.width / 2, size.height / 2),
                radius = size.minDimension / 2 - borderWidth / 2,
                style = Stroke(width = borderWidth)
            )
            val textLayoutResult = textMeasurer.measure(text = number.toString(), style = textStyle)
            drawText(
                textMeasurer = textMeasurer,
                text = number.toString(),
                topLeft = Offset(
                    x = (size.width - textLayoutResult.size.width) / 2,
                    y = (size.height - textLayoutResult.size.height) / 2
                ),
                style = textStyle
            )
        }
    }
}



/**
 * Renders a star icon that can be partially filled based on a rating value.
 *
 * @param rating The rating value out of 5 (e.g., 4.5).
 * @param modifier [Modifier] to be applied to the star container.
 * @param starSize The size of the star icon.
 * @param filledColor The color of the filled part of the star.
 * @param emptyColor The color of the background/unfilled part of the star.
 * @param starIcon The [ImageVector] to be used as the star shape.
 */
@Composable
fun PartialFillStar(
    rating: Float,
    modifier: Modifier = Modifier,
    starSize: Dp = 24.dp,
    filledColor: Color = Gold,
    emptyColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    starIcon: ImageVector = MaterialIcons.Filled.Star
) {
    val painter = rememberVectorPainter(image = starIcon)

    val fillPercentage = rating / 5f

    Box(modifier = modifier.size(starSize)) {
        // Empty part of the star (background)
        Canvas(modifier = Modifier.matchParentSize()) {
            with(painter) {
                draw(
                    size = this@Canvas.size,
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(emptyColor)
                )
            }
        }

        // Filled part of the star (foreground), clipped
        Canvas(modifier = Modifier.matchParentSize()) {
            val starWidth = this.size.width
            val filledWidth = starWidth * fillPercentage

            clipPath(Path().apply {
                addRect(Rect(Offset.Zero,
                    Size(filledWidth, this@Canvas.size.height)
                ))
            }) {
                with(painter) {
                    draw(
                        size = this@Canvas.size,
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(filledColor)
                    )
                }
            }
        }
    }
}

