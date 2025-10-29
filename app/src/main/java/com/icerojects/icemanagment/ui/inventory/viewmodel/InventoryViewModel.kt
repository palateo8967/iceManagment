package com.icerojects.icemanagment.ui.inventory.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icerojects.icemanagment.domain.model.Category
import com.icerojects.icemanagment.domain.model.Product
import com.icerojects.icemanagment.domain.model.UnitOfMeasure
import com.icerojects.icemanagment.domain.use_case.category.AddCategoryUseCase
import com.icerojects.icemanagment.domain.use_case.category.DeleteCategoryUseCase
import com.icerojects.icemanagment.domain.use_case.category.GetCategoriesUseCase
import com.icerojects.icemanagment.domain.use_case.product.AddProductUseCase
import com.icerojects.icemanagment.domain.use_case.product.DeleteProductUseCase
import com.icerojects.icemanagment.domain.use_case.product.FilterProductsByCategoryUseCase
import com.icerojects.icemanagment.domain.use_case.product.GetProductsUseCase
import com.icerojects.icemanagment.domain.use_case.product.SearchProductsUseCase
import com.icerojects.icemanagment.domain.use_case.product.UpdateProductUseCase
import com.icerojects.icemanagment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel for inventory management (products and categories)
 */
@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val filterProductsByCategoryUseCase: FilterProductsByCategoryUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : ViewModel() {

    // State for product list
    private val _productsState = mutableStateOf<ProductsState>(ProductsState())
    val productsState: State<ProductsState> = _productsState

    // State for categories list
    private val _categoriesState = mutableStateOf<CategoriesState>(CategoriesState())
    val categoriesState: State<CategoriesState> = _categoriesState

    // State for product form
    private val _productFormState = mutableStateOf(ProductFormState())
    val productFormState: State<ProductFormState> = _productFormState

    // State for category form
    private val _categoryFormState = mutableStateOf(CategoryFormState())
    val categoryFormState: State<CategoryFormState> = _categoryFormState

    // State for search filter
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    // State for category filter
    private val _selectedCategoryId = mutableStateOf<String?>(null)
    val selectedCategoryId: State<String?> = _selectedCategoryId

    // UI events
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow

    // Jobs for search debounce
    private var searchJob: Job? = null

    init {
        loadProducts()
        loadCategories()
    }

    // Functions to load data
    private fun loadProducts() {
        getProductsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _productsState.value = ProductsState(
                        items = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _productsState.value = ProductsState(
                        error = result.message ?: "Unknown error",
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _productsState.value = ProductsState(
                        items = result.data ?: emptyList(),
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadCategories() {
        getCategoriesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _categoriesState.value = CategoriesState(
                        categories = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _categoriesState.value = CategoriesState(
                        error = result.message ?: "Unknown error",
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _categoriesState.value = CategoriesState(
                        categories = result.data ?: emptyList(),
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    // Functions to handle UI events
    fun onEvent(event: InventoryEvent) {
        when (event) {
            is InventoryEvent.SearchProducts -> {
                _searchQuery.value = event.query
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L) // Debounce to avoid too many searches while typing
                    if (event.query.isBlank()) {
                        loadProducts()
                    } else {
                        searchProductsUseCase(event.query).onEach { result ->
                            when (result) {
                                is Resource.Success -> {
                                    _productsState.value = ProductsState(
                                        items = result.data ?: emptyList(),
                                        isLoading = false
                                    )
                                }
                                is Resource.Error -> {
                                    _productsState.value = ProductsState(
                                        error = result.message ?: "Search error",
                                        isLoading = false
                                    )
                                }
                                is Resource.Loading -> {
                                    _productsState.value = ProductsState(
                                        items = _productsState.value.items,
                                        isLoading = true
                                    )
                                }
                            }
                        }.launchIn(this)
                    }
                }
            }
            is InventoryEvent.FilterByCategory -> {
                _selectedCategoryId.value = event.categoryId
                if (event.categoryId == null) {
                    loadProducts()
                } else {
                    filterProductsByCategoryUseCase(event.categoryId).onEach { result ->
                        when (result) {
                            is Resource.Success -> {
                                _productsState.value = ProductsState(
                                    items = result.data ?: emptyList(),
                                    isLoading = false
                                )
                            }
                            is Resource.Error -> {
                                _productsState.value = ProductsState(
                                    error = result.message ?: "Filter error",
                                    isLoading = false
                                )
                            }
                            is Resource.Loading -> {
                                _productsState.value = ProductsState(
                                    items = _productsState.value.items,
                                    isLoading = true
                                )
                            }
                        }
                    }.launchIn(viewModelScope)
                }
            }
            is InventoryEvent.DeleteProduct -> {
                viewModelScope.launch {
                    val result = deleteProductUseCase(event.productId)
                    when (result) {
                        is Resource.Success -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar("Product deleted successfully"))
                        }
                        is Resource.Error -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Error deleting product"))
                        }
                        is Resource.Loading -> {
                            // Not necessary to handle loading state here
                        }
                    }
                }
            }
            is InventoryEvent.AddCategory -> {
                viewModelScope.launch {
                    if (_categoryFormState.value.name.isBlank()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Category name cannot be empty"))
                        return@launch
                    }

                    val category = Category(
                        name = _categoryFormState.value.name,
                        createdAt = Date()
                    )

                    val result = addCategoryUseCase(category)
                    when (result) {
                        is Resource.Success -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar("Category added successfully"))
                            _categoryFormState.value = CategoryFormState() // Reset form
                            _eventFlow.emit(UiEvent.CloseCategoryDialog)
                        }
                        is Resource.Error -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Error adding category"))
                        }
                        is Resource.Loading -> {
                            // Not necessary to handle loading state here
                        }
                    }
                }
            }
            is InventoryEvent.DeleteCategory -> {
                viewModelScope.launch {
                    val result = deleteCategoryUseCase(event.categoryId)
                    when (result) {
                        is Resource.Success -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar("Category deleted successfully"))
                        }
                        is Resource.Error -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Error deleting category"))
                        }
                        is Resource.Loading -> {
                            // Not necessary to handle loading state here
                        }
                    }
                }
            }
            is InventoryEvent.SaveProduct -> {
                viewModelScope.launch {
                    if (_productFormState.value.name.isBlank()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Product name cannot be empty"))
                        return@launch
                    }

                    if (_productFormState.value.categoryId.isBlank()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("You must select a category"))
                        return@launch
                    }

                    // Get the name of the selected category
                    val categoryName = _categoriesState.value.categories
                        .find { it.id == _productFormState.value.categoryId }?.name ?: "Uncategorized"

                    val product = Product(
                        id = _productFormState.value.id,
                        name = _productFormState.value.name,
                        categoryId = _productFormState.value.categoryId,
                        categoryName = categoryName,
                        quantity = _productFormState.value.quantity,
                        unit = _productFormState.value.unit,
                        minStock = _productFormState.value.minStock,
                        price = _productFormState.value.price,
                        createdAt = if (_productFormState.value.id.isBlank()) Date() else _productFormState.value.createdAt
                    )

                    val result = if (_productFormState.value.id.isBlank()) {
                        addProductUseCase(product)
                    } else {
                        updateProductUseCase(product).let { Resource.Success("") }
                    }

                    when (result) {
                        is Resource.Success -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar(
                                if (_productFormState.value.id.isBlank()) "Product added successfully"
                                else "Product updated successfully"
                            ))
                            _productFormState.value = ProductFormState() // Reset form
                            _eventFlow.emit(UiEvent.CloseProductDialog)
                        }
                        is Resource.Error -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Error saving product"))
                        }
                        is Resource.Loading -> {
                            // Not necessary to handle loading state here
                        }
                    }
                }
            }
            is InventoryEvent.EditProduct -> {
                _productFormState.value = ProductFormState(
                    id = event.product.id,
                    name = event.product.name,
                    categoryId = event.product.categoryId,
                    quantity = event.product.quantity,
                    unit = event.product.unit,
                    minStock = event.product.minStock,
                    price = event.product.price,
                    createdAt = event.product.createdAt
                )
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowProductDialog)
                }
            }
            is InventoryEvent.UpdateProductField -> {
                when (event.field) {
                    is ProductField.Name -> _productFormState.value = _productFormState.value.copy(name = event.field.value)
                    is ProductField.CategoryId -> _productFormState.value = _productFormState.value.copy(categoryId = event.field.value)
                    is ProductField.Quantity -> _productFormState.value = _productFormState.value.copy(quantity = event.field.value)
                    is ProductField.Unit -> _productFormState.value = _productFormState.value.copy(unit = event.field.value)
                    is ProductField.MinStock -> _productFormState.value = _productFormState.value.copy(minStock = event.field.value)
                    is ProductField.Price -> _productFormState.value = _productFormState.value.copy(price = event.field.value)
                }
            }
            is InventoryEvent.UpdateCategoryField -> {
                when (event.field) {
                    is CategoryField.Name -> _categoryFormState.value = _categoryFormState.value.copy(name = event.field.value)
                }
            }
            InventoryEvent.ResetProductForm -> {
                _productFormState.value = ProductFormState()
            }
            InventoryEvent.ResetCategoryForm -> {
                _categoryFormState.value = CategoryFormState()
            }
        }
    }

    // Classes to handle state
    data class ProductsState(
        val items: List<Product> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    data class CategoriesState(
        val categories: List<Category> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    data class ProductFormState(
        val id: String = "",
        val name: String = "",
        val categoryId: String = "",
        val quantity: Double = 0.0,
        val unit: UnitOfMeasure = UnitOfMeasure.UNIT,
        val minStock: Double = 0.0,
        val price: Double = 0.0,
        val createdAt: Date = Date()
    )

    data class CategoryFormState(
        val name: String = ""
    )

    // UI events
    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object CloseProductDialog : UiEvent()
        object ShowProductDialog : UiEvent()
        object CloseCategoryDialog : UiEvent()
    }

    // Inventory events
    sealed class InventoryEvent {
        data class SearchProducts(val query: String) : InventoryEvent()
        data class FilterByCategory(val categoryId: String?) : InventoryEvent()
        data class DeleteProduct(val productId: String) : InventoryEvent()
        data class EditProduct(val product: Product) : InventoryEvent()
        data class DeleteCategory(val categoryId: String) : InventoryEvent()
        object SaveProduct : InventoryEvent()
        object ResetProductForm : InventoryEvent()
        object ResetCategoryForm : InventoryEvent()
        data class UpdateProductField(val field: ProductField) : InventoryEvent()
        data class UpdateCategoryField(val field: CategoryField) : InventoryEvent()
        object AddCategory : InventoryEvent()
    }

    // Product form fields
    sealed class ProductField {
        data class Name(val value: String) : ProductField()
        data class CategoryId(val value: String) : ProductField()
        data class Quantity(val value: Double) : ProductField()
        data class Unit(val value: UnitOfMeasure) : ProductField()
        data class MinStock(val value: Double) : ProductField()
        data class Price(val value: Double) : ProductField()
    }

    // Category form fields
    sealed class CategoryField {
        data class Name(val value: String) : CategoryField()
    }
}