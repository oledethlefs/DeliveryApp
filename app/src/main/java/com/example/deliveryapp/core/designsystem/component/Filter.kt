package com.example.deliveryapp.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deliveryapp.R
import com.example.deliveryapp.core.data.model.Diet
import com.example.deliveryapp.core.data.model.ProductFilter
import com.example.deliveryapp.core.data.model.ProductSortingCategory
import com.example.deliveryapp.core.data.model.RecipeSortingCategory
import com.example.deliveryapp.core.designsystem.theme.DeliveryAppTheme
import kotlinx.coroutines.launch


/**
 * A modal bottom sheet that provides filtering and sorting options for recipes and products.
 *
 * This component dynamically displays sorting categories and filters based on the provided parameters.
 * It supports diet restrictions, recipe-specific sorting, and product-specific filtering/sorting.
 *
 * @param selectedDiets The list of currently selected [Diet] restrictions.
 * @param modifier The [Modifier] to be applied to the bottom sheet.
 * @param selectedRecipeSortingCategory The currently active [RecipeSortingCategory], if applicable.
 * @param selectedProductSortingCategory The currently active [ProductSortingCategory], if applicable.
 * @param selectedProductFilters The list of currently active [ProductFilter]s, if applicable.
 * @param productFilters The complete list of available [ProductFilter]s to display, if applicable.
 * @param onProductSortingCategorySelected A callback to be invoked when a product sorting category is selected.
 * @param onRecipeSortingCategorySelected A callback to be invoked when a recipe sorting category is selected.
 * @param onProductFilterSelected A callback to be invoked when a product filter is selected.
 * @param onDietSelected A callback to be invoked when a diet restriction is selected.
 * @param onResetFilters A callback to be invoked when the reset button is clicked.
 * @param onCloseFilterSheet A callback to be invoked when the bottom sheet is closed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterAndSortSheet(
    selectedDiets: List<Diet>,
    modifier: Modifier = Modifier,
    selectedRecipeSortingCategory: RecipeSortingCategory? = null,
    selectedProductSortingCategory: ProductSortingCategory? = null,
    selectedProductFilters: List<ProductFilter>? = null,
    productFilters: List<ProductFilter>? = null,
    onProductSortingCategorySelected: (ProductSortingCategory) -> Unit = {},
    onRecipeSortingCategorySelected: (RecipeSortingCategory) -> Unit = {},
    onProductFilterSelected: (ProductFilter) -> Unit = {},
    onDietSelected: (List<Diet>) -> Unit,
    onResetFilters: () -> Unit,
    onCloseFilterSheet: () -> Unit
) {
    val sheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true) // Keep it fully expanded
    val scope = rememberCoroutineScope()

    val haptic = LocalHapticFeedback.current

    ModalBottomSheet(
        onDismissRequest = onCloseFilterSheet,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (selectedRecipeSortingCategory != null) {
                    RecipeSortingCategoryFilter(
                        allSortOptions = RecipeSortingCategory.entries.toList(),
                        currentSortOption = selectedRecipeSortingCategory,
                        onSortOptionSelected = { onRecipeSortingCategorySelected(it) }
                    )
                }
                if (selectedProductSortingCategory != null) {
                    ProductSortingCategoryFilter(
                        allSortOptions = ProductSortingCategory.entries.toList(),
                        currentSortOption = selectedProductSortingCategory,
                        onSortOptionSelected = { onProductSortingCategorySelected(it) }
                    )

                }
                if (productFilters != null && selectedProductFilters != null) {
                    ProductFilterList(
                        productFilters = productFilters,
                        selectedProductFilters = selectedProductFilters,
                        onFilterSelected = { onProductFilterSelected(it) }
                    )
                }

                DietsFilter(
                    allDiets = Diet.entries.toList(),
                    currentSelectedDiets = selectedDiets,
                    onDietSelected = { onDietSelected(it) }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DoneButton {
                    scope.launch {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        sheetState.hide()
                        onCloseFilterSheet()
                    }
                }

                ResetButton {
                    onResetFilters()
                }
            }

        }
    }
}

/**
 * A composable function that displays a list of diet filters with checkboxes.
 *
 * @param allDiets The list of all available [Diet] options to display.
 * @param currentSelectedDiets The list of currently active [Diet] filters.
 * @param onDietSelected Callback invoked when a diet is toggled, providing the updated list of selected diets.
 * @param modifier The [Modifier] to be applied to the column container.
 */
@Composable
fun DietsFilter(
    allDiets: List<Diet>,
    currentSelectedDiets: List<Diet>,
    onDietSelected: (List<Diet>) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.diets),
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(bottom = 8.dp),
        style = MaterialTheme.typography.titleMedium
    )
    Column(modifier) {
        allDiets.forEach { diet ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (diet in currentSelectedDiets),
                        onClick = {
                            onDietSelected(
                                if (diet in currentSelectedDiets) currentSelectedDiets - diet else currentSelectedDiets + diet
                            )
                        },
                        role = Role.Checkbox
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = diet.label),
                    modifier = Modifier.weight(1f),
                    fontWeight = if (diet in currentSelectedDiets) FontWeight.Bold else FontWeight.Normal,
                    color = if (diet in currentSelectedDiets) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Checkbox(
                    checked = (diet in currentSelectedDiets),
                    onCheckedChange = null
                )
            }
        }
    }

}

/**
 * A UI component that displays a list of recipe sorting categories as selectable radio buttons.
 *
 * @param allSortOptions The list of all available [RecipeSortingCategory] options to be displayed.
 * @param currentSortOption The currently active [RecipeSortingCategory].
 * @param onSortOptionSelected Callback invoked when a new sorting category is selected.
 * @param modifier The [Modifier] to be applied to the layout.
 */
@Composable
fun RecipeSortingCategoryFilter(
    allSortOptions: List<RecipeSortingCategory>,
    currentSortOption: RecipeSortingCategory,
    onSortOptionSelected: (RecipeSortingCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.sort_by),
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(bottom = 8.dp),
        style = MaterialTheme.typography.titleMedium
    )
    Column(
        modifier
            .selectableGroup()
    ) {
        allSortOptions.forEach { sortOption ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (sortOption == currentSortOption),
                        onClick = { onSortOptionSelected(sortOption) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = sortOption.label),
                    modifier = Modifier.weight(1f),
                    fontWeight = if (sortOption == currentSortOption) FontWeight.Bold else FontWeight.Normal,
                    color = if (sortOption == currentSortOption) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                RadioButton(
                    selected = (sortOption == currentSortOption),
                    onClick = null
                )
            }
        }
    }
    HorizontalDivider()
}

/**
 * A composable that displays a list of product sorting options as a group of radio buttons.
 *
 * @param allSortOptions The list of all available [ProductSortingCategory] options.
 * @param currentSortOption The currently selected [ProductSortingCategory].
 * @param onSortOptionSelected Callback triggered when a new sorting option is selected.
 * @param modifier The [Modifier] to be applied to the column layout.
 */
@Composable
fun ProductSortingCategoryFilter(
    allSortOptions: List<ProductSortingCategory>,
    currentSortOption: ProductSortingCategory,
    onSortOptionSelected: (ProductSortingCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.sort_by),
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(bottom = 8.dp),
        style = MaterialTheme.typography.titleMedium
    )
    Column(
        modifier
            .selectableGroup()
    ) {
        allSortOptions.forEach { sortOption ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (sortOption == currentSortOption),
                        onClick = { onSortOptionSelected(sortOption) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = sortOption.label),
                    modifier = Modifier.weight(1f),
                    fontWeight = if (sortOption == currentSortOption) FontWeight.Bold else FontWeight.Normal,
                    color = if (sortOption == currentSortOption) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                RadioButton(
                    selected = (sortOption == currentSortOption),
                    onClick = null
                )
            }
        }
    }
    HorizontalDivider()
}

/**
 * A composable that displays a list of product filters, separated into categories such as brands
 * and other general filters.
 *
 * @param productFilters The complete list of available [ProductFilter] options to display.
 * @param selectedProductFilters The list of [ProductFilter]s that are currently active or selected.
 * @param onFilterSelected Callback invoked when a filter is clicked, providing the selected [ProductFilter].
 * @param modifier The [Modifier] to be applied to the filter list layout.
 */
@Composable
fun ProductFilterList(
    productFilters: List<ProductFilter>,
    selectedProductFilters: List<ProductFilter>,
    onFilterSelected: (ProductFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val brands = productFilters.filter { it.isBrandFilter }
    val otherFilters = productFilters.filter { !it.isBrandFilter }

    if (brands.isNotEmpty()) {
        Text(
            text = stringResource(id = R.string.brands),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Column(modifier) {
            brands.forEach { brand ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (brand in selectedProductFilters),
                            onClick = { onFilterSelected(brand) },
                            role = Role.Checkbox
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = brand.name,
                        modifier = Modifier.weight(1f),
                        fontWeight = if (brand in selectedProductFilters) FontWeight.Bold else FontWeight.Normal,
                        color = if (brand in selectedProductFilters) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    Checkbox(
                        checked = (brand in selectedProductFilters),
                        onCheckedChange = null
                    )
                }
            }
        }
        HorizontalDivider()
    }
    if (otherFilters.isNotEmpty()) {
        Text(
            text = stringResource(id = R.string.other_product_filters),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Column(modifier) {
            otherFilters.forEach { filter ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (filter in selectedProductFilters),
                            onClick = { onFilterSelected(filter) },
                            role = Role.Checkbox
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = filter.name,
                        modifier = Modifier.weight(1f),
                        fontWeight = if (filter in selectedProductFilters) FontWeight.Bold else FontWeight.Normal,
                        color = if (filter in selectedProductFilters) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    Checkbox(
                        checked = (filter in selectedProductFilters),
                        onCheckedChange = null
                    )
                }
            }
        }
        HorizontalDivider()
    }
}


@Preview
@Composable
fun RecipeFilterPreview() {
    DeliveryAppTheme {
        FilterAndSortSheet(
            selectedDiets = listOf(Diet.VEGAN, Diet.LOW_CARB),
            selectedRecipeSortingCategory = RecipeSortingCategory.POPULARITY,
            onCloseFilterSheet = {},
            onDietSelected = {},
            onResetFilters = {}
        )
    }
}

@Preview
@Composable
fun ProductFilterPreview() {
    DeliveryAppTheme {
        FilterAndSortSheet(
            selectedDiets = listOf(Diet.VEGAN, Diet.LOW_CARB),
            selectedProductSortingCategory = ProductSortingCategory.POPULARITY,
            onCloseFilterSheet = {},
            onDietSelected = {},
            onResetFilters = {},
            productFilters = listOf(ProductFilter(1, "fritz", true), ProductFilter(2, "Coca-Cola", true), ProductFilter(3, "Sinalco", true), ProductFilter(4, "Pepsi", true)),
            selectedProductFilters = listOf(ProductFilter(1, "fritz", true), ProductFilter(2, "Coca-Cola", true))
        )
    }
}
