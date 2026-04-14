package com.example.deliveryapp.feature.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.deliveryapp.core.data.model.Diet
import com.example.deliveryapp.core.data.model.Recipe
import com.example.deliveryapp.core.data.model.RecipeCategory
import com.example.deliveryapp.core.data.model.RecipeSortingCategory
import com.example.deliveryapp.core.data.repository.CheckoutRepository
import com.example.deliveryapp.core.data.repository.RecipeRepository
import com.example.deliveryapp.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and business logic of the recipes screen.
 *
 * @property userDataRepository Repository for managing user-specific data like favorites and diets.
 * @property recipesRepository Repository for fetching recipe categories and paginated recipe lists.
 * @property checkoutRepository Repository for managing the shopping cart and checkout process.
 */
@HiltViewModel
@kotlinx.coroutines.ExperimentalCoroutinesApi
class RecipesViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val recipesRepository: RecipeRepository,
    private val checkoutRepository: CheckoutRepository
) : ViewModel() {


    private val _selectedRecipeCategory = MutableStateFlow(RecipeCategory.STARTER)
    val selectedRecipeCategory = _selectedRecipeCategory.asStateFlow()

    private val _selectedRecipeSortingCategory = MutableStateFlow(RecipeSortingCategory.POPULARITY)
    val selectedRecipeSortingCategory = _selectedRecipeSortingCategory.asStateFlow()


    /**
     * A [StateFlow] emitting a list of IDs for recipes that the user has bookmarked.
     */
    val bookmarkedRecipeIds: StateFlow<List<Int>> = userDataRepository.userData
        .map { it.bookmarkedRecipeIds }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * A [StateFlow] representing the list of dietary restrictions or preferences selected by the user.
     */
    val selectedDiets: StateFlow<List<Diet>> = userDataRepository.userDiets
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    /**
     * A map that associates each [RecipeCategory] with a [Flow] of [PagingData] for [Recipe]s.
     *
     * Each flow is reactively updated whenever the [selectedDiets] or [selectedRecipeSortingCategory]
     * changes.
     */
    private val recipesByCategoryFlows =
        RecipeCategory.entries.associateWith { recipeCategory ->
            combine(
                selectedDiets,
                selectedRecipeSortingCategory
            ) { diets, recipeSortingCategory ->
                Triple(recipeCategory, diets, recipeSortingCategory)
            }.flatMapLatest { (recipeCategory, diets, recipeSortingCategory) ->
                recipesRepository.getRecipes(
                    recipeCategory = recipeCategory,
                    diets = diets,
                    sortingCategory = recipeSortingCategory
                ).cachedIn(viewModelScope)
            }
        }.toMap()


    /**
     * Retrieves a flow of paginated recipe data for a specific category.
     *
     * @param category The [RecipeCategory] used to filter the recipes.
     * @return A [Flow] of [PagingData] containing the [Recipe] objects associated with the given category.
     */
    fun getRecipesForCategory(category: RecipeCategory): Flow<PagingData<Recipe>> {
        return recipesByCategoryFlows[category] ?: emptyFlow()


    }


    /**
     * Updates the user's selected dietary preferences in the [UserDataRepository].
     *
     * @param newDiets The new list of [Diet] categories to be persisted and applied as filters.
     */
    fun updateSelectedDiets(newDiets: List<Diet>) {
        viewModelScope.launch {
            userDataRepository.updateDiets(newDiets)
        }
    }


    /**
     * Updates the bookmarked status of the given [recipe].
     *
     * @param recipe The [Recipe] to be bookmarked or removed from bookmarks.
     */
    fun bookmarkRecipe(recipe: Recipe) {
        val recipeId = recipe.id
        viewModelScope.launch {
            userDataRepository.setRecipeBookmarked(recipeId)
        }
    }

    /**
     * Updates the currently selected recipe category.
     *
     * @param category The new [RecipeCategory] to be applied.
     */
    fun setRecipeCategory(category: RecipeCategory) {
        _selectedRecipeCategory.value = category

    }

    /**
     * Updates the currently selected recipe sorting category.
     */
    fun setSortingCategory(category: RecipeSortingCategory) {
        _selectedRecipeSortingCategory.value = category

    }

    /**
     * Adds all the products from the given [recipe] to the user's shopping cart.
     *
     * @param recipe The [Recipe] to be added to the shopping cart.
     */
    fun addRecipeToShoppingCart(recipe: Recipe) {
        viewModelScope.launch {
            checkoutRepository.addProductsFromRecipeToCart(recipe.id)
        }
    }

    /**
     * Deletes the products from the given [recipe] from the user's shopping cart.
     *
     * @param recipe The [Recipe] to be added to the shopping cart.
     */
    fun deleteRecipeFromShoppingCart(recipe: Recipe) {
        viewModelScope.launch {
            checkoutRepository.deleteProductsFromRecipeFromCart(recipe.id)
        }
    }


}