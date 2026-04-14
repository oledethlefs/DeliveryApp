package com.example.deliveryapp.feature.products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.deliveryapp.core.data.model.CartProduct
import com.example.deliveryapp.core.data.model.Diet
import com.example.deliveryapp.core.data.model.Product
import com.example.deliveryapp.core.data.model.ProductCategory
import com.example.deliveryapp.core.data.model.ProductFilter
import com.example.deliveryapp.core.data.model.ProductSortingCategory
import com.example.deliveryapp.core.data.repository.CheckoutRepository
import com.example.deliveryapp.core.data.repository.ProductRepository
import com.example.deliveryapp.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and business logic of the products screen.
 *
 *
 * @property userDataRepository Repository for managing user-specific data like favorites and diets.
 * @property checkoutRepository Repository for managing the shopping cart and checkout process.
 * @property productsRepository Repository for fetching product categories and paginated product lists.
 */
@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val checkoutRepository: CheckoutRepository,
    private val productsRepository: ProductRepository
) : ViewModel() {

    val cartItems: StateFlow<List<CartProduct>> = checkoutRepository.productsInCart
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    private val _selectedProductCategoryId = MutableStateFlow(0)
    val selectedProductCategoryId = _selectedProductCategoryId.asStateFlow()

    private val _sortingCategory = MutableStateFlow(ProductSortingCategory.POPULARITY)
    val sortingCategory = _sortingCategory.asStateFlow()

    private val _selectedProductFilters = MutableStateFlow<List<ProductFilter>>(emptyList())
    val selectedProductFilters = _selectedProductFilters.asStateFlow()
    fun setProductCategoryId(categoryId: Int) {
        _selectedProductCategoryId.value = categoryId
    }

    /**
     * Toggles the selection state of a specific [ProductFilter].
     *
     * If the filter is already present in the current selection, it will be removed.
     * If it is not present, it will be added to the selection list.
     *
     * @param filter The [ProductFilter] to be added or removed from the current filter set.
     */
    fun toggleProductFilter(filter: ProductFilter) {
        val currentFilters = _selectedProductFilters.value
        _selectedProductFilters.value = if (currentFilters.contains(filter)) {
            currentFilters - filter
        } else {
            currentFilters + filter
        }
    }

    fun setSortingCategory(category: ProductSortingCategory) {
        _sortingCategory.value = category
    }

    fun setProductFavorite(product: Product) {
        val productId = product.id
        viewModelScope.launch {
            userDataRepository.setProductFavorite(productId)
        }
    }

    fun addProductToCart(product: Product) {
        viewModelScope.launch {
            checkoutRepository.addProductsToCart(listOf(product.id))
        }
    }

    /**
     * A [StateFlow] emitting a list of IDs for products that the user has marked as favorites.
     */
    val favoriteProductsIds: StateFlow<List<Int>> = userDataRepository.userData
        .map { it.favoriteProductsIds }
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
     * A [StateFlow] representing the current UI state of the product categories.
     *
     * It emits:
     * - [CategoryUiState.Loading] when a fetch is initiated.
     * - [CategoryUiState.Success] containing the [ProductCategory] when the data is retrieved.
     * - [CategoryUiState.Error] if the fetch operation fails.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val categoryUiState: StateFlow<CategoryUiState> = selectedProductCategoryId
        .flatMapLatest { categoryId ->
            flow {
                emit(CategoryUiState.Loading)
                try {
                    val category = productsRepository.getProductCategories(categoryId)
                    emit(CategoryUiState.Success(category))
                } catch (e: Exception) {
                    Log.e("ProductsViewModel", "Error fetching product categories", e)
                    emit(CategoryUiState.Error(e.message ?: "Unknown Error"))
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CategoryUiState.Loading
        )


    /**
     * A [StateFlow] providing the list of available [ProductFilter]s for the currently
     * selected category.
     */
    val productFilters: StateFlow<List<ProductFilter>> = categoryUiState
        .map { state ->
            if (state is CategoryUiState.Success) {
                state.category.productFilters
            } else {
                emptyList()
            }
        }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    /**
     * A reactive stream of paginated [Product] data.
     *
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val products: Flow<PagingData<Product>> = combine(
        selectedProductCategoryId,
        selectedDiets,
        sortingCategory,
        selectedProductFilters
    ) { categoryId, diets, sorting, filters ->
        DataTuple(categoryId, diets, sorting, filters)
    }.flatMapLatest { tuple ->
        productsRepository.getProducts(
            productCategoryId = tuple.categoryId,
            diets = tuple.diets,
            sortingCategory = tuple.sorting,
            productFilters = tuple.filters
        )
    }.cachedIn(viewModelScope)

    /**
     * Decreases the quantity of a specific item in the shopping cart.
     *
     *
     * @param productId The unique identifier of the product whose quantity should be decreased.
     */
    fun decreaseQuantityOfCartItem(productId: Int) {
        viewModelScope.launch {
            checkoutRepository.decreaseQuantityOfCartItem(productId)
        }

    }

    /**
     * Increases the quantity of a specific item in the shopping cart.
     *
     *
     * @param productId The unique identifier of the product whose quantity should be increased.
     */
    fun increaseQuantityOfCartItem(productId: Int) {
        viewModelScope.launch {
            checkoutRepository.increaseQuantityOfCartItem(productId)
        }
    }

    private data class DataTuple(
        val categoryId: Int,
        val diets: List<Diet>,
        val sorting: ProductSortingCategory,
        val filters: List<ProductFilter>
    )

    sealed class CategoryUiState {
        data object Loading : CategoryUiState()
        data class Success(val category: ProductCategory) : CategoryUiState()
        data class Error(val message: String) : CategoryUiState()
    }

}

