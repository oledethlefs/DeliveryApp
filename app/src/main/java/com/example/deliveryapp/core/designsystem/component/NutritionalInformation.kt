package com.example.deliveryapp.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deliveryapp.R
import com.example.deliveryapp.core.data.model.Product
import com.example.deliveryapp.core.data.model.Recipe
import com.example.deliveryapp.core.data.model.fritzKolaSingle033
import com.example.deliveryapp.core.data.model.steakRecipe
import com.example.deliveryapp.core.designsystem.theme.DeliveryAppTheme






/**
 * A composable component that displays a detailed breakdown of nutritional information for a [Product].
 *
 * It renders a title followed by a list of nutritional facts including calories (kJ/kcal),
 * fats, saturated fats, carbohydrates, sugars, proteins, and sodium, formatted in alternating
 * background rows for better readability.
 *
 * @param product The product data model containing the nutritional values to display.
 */
@Composable
fun ProductNutritionalInformation(product: Product) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.product_nutritional_information),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Column(modifier = Modifier.clip(RoundedCornerShape(8.dp))) {
            // Calories
            NutritionalRow(
                label = stringResource(id = R.string.calories),
                value = "${kcalToKj(product.calories)} kJ \n / ${product.calories} kcal",
                isVariant = true
            )

            // Fats
            NutritionalRow(
                label = stringResource(id = R.string.fats),
                value = "${formatNutrient(product.fat)} g",
                isVariant = false
            )
            NutritionalRow(
                label = stringResource(id = R.string.saturated_fats),
                value = "${formatNutrient(product.saturatedFat)} g",
                isVariant = false
            )

            // Carbs
            NutritionalRow(
                label = stringResource(id = R.string.carbs),
                value = "${formatNutrient(product.carbs)} g",
                isVariant = true
            )
            NutritionalRow(
                label = stringResource(id = R.string.sugar),
                value = "${formatNutrient(product.sugar)} g",
                isVariant = true
            )

            // Proteins
            NutritionalRow(
                label = stringResource(id = R.string.proteins),
                value = "${formatNutrient(product.protein)} g",
                isVariant = false
            )

            // Sodium
            NutritionalRow(
                label = stringResource(id = R.string.sodium),
                value = "${formatNutrient(product.sodium)} g",
                isVariant = true
            )
        }
    }
}

/**
 * Shared helper to format 0.0f as 0 for cleaner UI
 */
private fun formatNutrient(value: Float): String {
    return if (value == 0.0f) "0" else value.toString()
}

/**
 * Displays the nutritional information for a specific [recipe].
 *
 * This component renders a list of nutritional values including calories (kJ/kcal),
 * carbohydrates, fiber, proteins, and fats, presented in a structured layout with
 * alternating row styles for better readability.
 *
 * @param recipe The [Recipe] data object containing the nutritional values to display.
 */
@Composable
fun RecipeNutritionalInformation(recipe: Recipe) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.recipe_nutritional_information),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Column(modifier = Modifier.clip(RoundedCornerShape(8.dp))) {
            // Calories
            NutritionalRow(
                label = stringResource(id = R.string.calories),
                value = "${kcalToKj(recipe.calories)} kJ \n / ${recipe.calories} kcal",
                isVariant = true
            )
            // Carbs
            NutritionalRow(
                label = stringResource(id = R.string.carbs),
                value = "${recipe.carbs} g",
                isVariant = false
            )
            // Fiber
            NutritionalRow(
                label = stringResource(id = R.string.fiber),
                value = "${recipe.fiber} g",
                isVariant = true
            )
            // Proteins
            NutritionalRow(
                label = stringResource(id = R.string.proteins),
                value = "${recipe.protein} g",
                isVariant = false
            )
            // Fats
            NutritionalRow(
                label = stringResource(id = R.string.fats),
                value = "${recipe.fat} g",
                isVariant = true
            )
        }
    }
}

/**
 * The Helper Component used by both Product and Recipe information
 */
@Composable
private fun NutritionalRow(
    label: String,
    value: String,
    isVariant: Boolean
) {
    Row(
        modifier = Modifier
            .background(
                if (isVariant) MaterialTheme.colorScheme.surfaceVariant
                else MaterialTheme.colorScheme.surface
            )
            .padding(12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeNutritionalInformationPreview() {

    DeliveryAppTheme {
        RecipeNutritionalInformation(recipe = steakRecipe)
    }
}


@Preview(showBackground = true)
@Composable
fun ProductNutritionalInformationPreview() {
    DeliveryAppTheme {
        ProductNutritionalInformation(product = fritzKolaSingle033)
    }
}

/**
 * Converts kilocalories (kcal) to kilojoules (kJ).
 *
 * @param kcal The energy value in kilocalories to be converted.
 * @return The converted value in kilojoules as an integer.
 */
fun kcalToKj(kcal: Int): Int {
    return (kcal * 4.184).toInt()
}