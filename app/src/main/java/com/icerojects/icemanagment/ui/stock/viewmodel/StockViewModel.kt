package com.icerojects.icemanagment.ui.stock.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icerojects.icemanagment.domain.model.Categoria
import com.icerojects.icemanagment.domain.model.StockItem
import com.icerojects.icemanagment.domain.model.UnidadMedida
import com.icerojects.icemanagment.domain.use_case.categoria.AddCategoriaUseCase
import com.icerojects.icemanagment.domain.use_case.categoria.DeleteCategoriaUseCase
import com.icerojects.icemanagment.domain.use_case.categoria.GetCategoriasUseCase
import com.icerojects.icemanagment.domain.use_case.stock.AddStockItemUseCase
import com.icerojects.icemanagment.domain.use_case.stock.DeleteStockItemUseCase
import com.icerojects.icemanagment.domain.use_case.stock.FilterStockItemsByCategoryUseCase
import com.icerojects.icemanagment.domain.use_case.stock.GetStockItemsUseCase
import com.icerojects.icemanagment.domain.use_case.stock.SearchStockItemsUseCase
import com.icerojects.icemanagment.domain.use_case.stock.UpdateStockItemUseCase
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

@HiltViewModel
class StockViewModel @Inject constructor(
    private val getStockItemsUseCase: GetStockItemsUseCase,
    private val addStockItemUseCase: AddStockItemUseCase,
    private val updateStockItemUseCase: UpdateStockItemUseCase,
    private val deleteStockItemUseCase: DeleteStockItemUseCase,
    private val searchStockItemsUseCase: SearchStockItemsUseCase,
    private val filterStockItemsByCategoryUseCase: FilterStockItemsByCategoryUseCase,
    private val getCategoriasUseCase: GetCategoriasUseCase,
    private val addCategoriaUseCase: AddCategoriaUseCase,
    private val deleteCategoriaUseCase: DeleteCategoriaUseCase
) : ViewModel() {

    // Estado para la lista de productos
    private val _stockState = mutableStateOf<StockState>(StockState())
    val stockState: State<StockState> = _stockState

    // Estado para la lista de categorías
    private val _categoriasState = mutableStateOf<CategoriasState>(CategoriasState())
    val categoriasState: State<CategoriasState> = _categoriasState

    // Estado para el formulario de producto
    private val _productFormState = mutableStateOf(ProductFormState())
    val productFormState: State<ProductFormState> = _productFormState

    // Estado para el formulario de categoría
    private val _categoriaFormState = mutableStateOf(CategoriaFormState())
    val categoriaFormState: State<CategoriaFormState> = _categoriaFormState

    // Estado para el filtro de búsqueda
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    // Estado para el filtro de categoría
    private val _selectedCategoryId = mutableStateOf<String?>(null)
    val selectedCategoryId: State<String?> = _selectedCategoryId

    // Eventos UI
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow

    // Jobs para debounce de búsqueda
    private var searchJob: Job? = null

    init {
        loadStockItems()
        loadCategorias()
    }

    // Funciones para cargar datos
    private fun loadStockItems() {
        getStockItemsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _stockState.value = StockState(
                        items = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _stockState.value = StockState(
                        error = result.message ?: "Error desconocido",
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Error desconocido"))
                }
                is Resource.Loading -> {
                    _stockState.value = StockState(
                        items = result.data ?: emptyList(),
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadCategorias() {
        getCategoriasUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _categoriasState.value = CategoriasState(
                        categorias = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _categoriasState.value = CategoriasState(
                        error = result.message ?: "Error desconocido",
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Error desconocido"))
                }
                is Resource.Loading -> {
                    _categoriasState.value = CategoriasState(
                        categorias = result.data ?: emptyList(),
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    // Funciones para manejar eventos de UI
    fun onEvent(event: StockEvent) {
        when (event) {
            is StockEvent.SearchProducts -> {
                _searchQuery.value = event.query
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L) // Debounce para evitar muchas búsquedas mientras se escribe
                    if (event.query.isBlank()) {
                        loadStockItems()
                    } else {
                        searchStockItemsUseCase(event.query).onEach { result ->
                            when (result) {
                                is Resource.Success -> {
                                    _stockState.value = StockState(
                                        items = result.data ?: emptyList(),
                                        isLoading = false
                                    )
                                }
                                is Resource.Error -> {
                                    _stockState.value = StockState(
                                        error = result.message ?: "Error en la búsqueda",
                                        isLoading = false
                                    )
                                }
                                is Resource.Loading -> {
                                    _stockState.value = StockState(
                                        items = _stockState.value.items,
                                        isLoading = true
                                    )
                                }
                            }
                        }.launchIn(this)
                    }
                }
            }
            is StockEvent.FilterByCategory -> {
                _selectedCategoryId.value = event.categoryId
                if (event.categoryId == null) {
                    loadStockItems()
                } else {
                    filterStockItemsByCategoryUseCase(event.categoryId).onEach { result ->
                        when (result) {
                            is Resource.Success -> {
                                _stockState.value = StockState(
                                    items = result.data ?: emptyList(),
                                    isLoading = false
                                )
                            }
                            is Resource.Error -> {
                                _stockState.value = StockState(
                                    error = result.message ?: "Error al filtrar",
                                    isLoading = false
                                )
                            }
                            is Resource.Loading -> {
                                _stockState.value = StockState(
                                    items = _stockState.value.items,
                                    isLoading = true
                                )
                            }
                        }
                    }.launchIn(viewModelScope)
                }
            }
            is StockEvent.DeleteProduct -> {
                viewModelScope.launch {
                    val result = deleteStockItemUseCase(event.productId)
                    when (result) {
                        is Resource.Success -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar("Producto eliminado correctamente"))
                        }
                        is Resource.Error -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Error al eliminar el producto"))
                        }
                        is Resource.Loading -> {
                            // No es necesario manejar el estado de carga aquí
                        }
                    }
                }
            }
            is StockEvent.AddCategory -> {
                viewModelScope.launch {
                    if (_categoriaFormState.value.nombre.isBlank()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("El nombre de la categoría no puede estar vacío"))
                        return@launch
                    }

                    val categoria = Categoria(
                        nombre = _categoriaFormState.value.nombre,
                        fechaCreacion = Date()
                    )

                    val result = addCategoriaUseCase(categoria)
                    when (result) {
                        is Resource.Success -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar("Categoría agregada correctamente"))
                            _categoriaFormState.value = CategoriaFormState() // Resetear el formulario
                            _eventFlow.emit(UiEvent.CloseCategoriaDialog)
                        }
                        is Resource.Error -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Error al agregar la categoría"))
                        }
                        is Resource.Loading -> {
                            // No es necesario manejar el estado de carga aquí
                        }
                    }
                }
            }
            is StockEvent.DeleteCategory -> {
                viewModelScope.launch {
                    val result = deleteCategoriaUseCase(event.categoryId)
                    when (result) {
                        is Resource.Success -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar("Categoría eliminada correctamente"))
                        }
                        is Resource.Error -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Error al eliminar la categoría"))
                        }
                        is Resource.Loading -> {
                            // No es necesario manejar el estado de carga aquí
                        }
                    }
                }
            }
            is StockEvent.SaveProduct -> {
                viewModelScope.launch {
                    if (_productFormState.value.nombre.isBlank()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("El nombre del producto no puede estar vacío"))
                        return@launch
                    }

                    if (_productFormState.value.categoriaId.isBlank()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Debe seleccionar una categoría"))
                        return@launch
                    }

                    // Obtener el nombre de la categoría seleccionada
                    val categoriaNombre = _categoriasState.value.categorias
                        .find { it.id == _productFormState.value.categoriaId }?.nombre ?: "Sin categoría"

                    val stockItem = StockItem(
                        id = _productFormState.value.id,
                        nombre = _productFormState.value.nombre,
                        categoriaId = _productFormState.value.categoriaId,
                        categoriaNombre = categoriaNombre,
                        cantidad = _productFormState.value.cantidad,
                        unidad = _productFormState.value.unidad,
                        stockMinimo = _productFormState.value.stockMinimo,
                        precio = _productFormState.value.precio,
                        fechaCreacion = if (_productFormState.value.id.isBlank()) Date() else _productFormState.value.fechaCreacion
                    )

                    val result = if (_productFormState.value.id.isBlank()) {
                        addStockItemUseCase(stockItem)
                    } else {
                        updateStockItemUseCase(stockItem).let { Resource.Success("") }
                    }

                    when (result) {
                        is Resource.Success -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar(
                                if (_productFormState.value.id.isBlank()) "Producto agregado correctamente"
                                else "Producto actualizado correctamente"
                            ))
                            _productFormState.value = ProductFormState() // Resetear el formulario
                            _eventFlow.emit(UiEvent.CloseProductDialog)
                        }
                        is Resource.Error -> {
                            _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Error al guardar el producto"))
                        }
                        is Resource.Loading -> {
                            // No es necesario manejar el estado de carga aquí
                        }
                    }
                }
            }
            is StockEvent.EditProduct -> {
                _productFormState.value = ProductFormState(
                    id = event.product.id,
                    nombre = event.product.nombre,
                    categoriaId = event.product.categoriaId,
                    cantidad = event.product.cantidad,
                    unidad = event.product.unidad,
                    stockMinimo = event.product.stockMinimo,
                    precio = event.product.precio,
                    fechaCreacion = event.product.fechaCreacion
                )
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowProductDialog)
                }
            }
            is StockEvent.UpdateProductField -> {
                when (event.field) {
                    is ProductField.Nombre -> _productFormState.value = _productFormState.value.copy(nombre = event.field.value)
                    is ProductField.CategoriaId -> _productFormState.value = _productFormState.value.copy(categoriaId = event.field.value)
                    is ProductField.Cantidad -> _productFormState.value = _productFormState.value.copy(cantidad = event.field.value)
                    is ProductField.Unidad -> _productFormState.value = _productFormState.value.copy(unidad = event.field.value)
                    is ProductField.StockMinimo -> _productFormState.value = _productFormState.value.copy(stockMinimo = event.field.value)
                    is ProductField.Precio -> _productFormState.value = _productFormState.value.copy(precio = event.field.value)
                }
            }
            is StockEvent.UpdateCategoriaField -> {
                when (event.field) {
                    is CategoriaField.Nombre -> _categoriaFormState.value = _categoriaFormState.value.copy(nombre = event.field.value)
                }
            }
            StockEvent.ResetProductForm -> {
                _productFormState.value = ProductFormState()
            }
            StockEvent.ResetCategoriaForm -> {
                _categoriaFormState.value = CategoriaFormState()
            }
        }
    }

    // Clases para manejar el estado
    data class StockState(
        val items: List<StockItem> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    data class CategoriasState(
        val categorias: List<Categoria> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    data class ProductFormState(
        val id: String = "",
        val nombre: String = "",
        val categoriaId: String = "",
        val cantidad: Double = 0.0,
        val unidad: UnidadMedida = UnidadMedida.UNIDAD,
        val stockMinimo: Double = 0.0,
        val precio: Double = 0.0,
        val fechaCreacion: Date = Date()
    )

    data class CategoriaFormState(
        val nombre: String = ""
    )

    // Eventos de UI
    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object CloseProductDialog : UiEvent()
        object ShowProductDialog : UiEvent()
        object CloseCategoriaDialog : UiEvent()
    }

    // Eventos de Stock
    sealed class StockEvent {
        data class SearchProducts(val query: String) : StockEvent()
        data class FilterByCategory(val categoryId: String?) : StockEvent()
        data class DeleteProduct(val productId: String) : StockEvent()
        data class EditProduct(val product: StockItem) : StockEvent()
        data class DeleteCategory(val categoryId: String) : StockEvent()
        object SaveProduct : StockEvent()
        object ResetProductForm : StockEvent()
        object ResetCategoriaForm : StockEvent()
        data class UpdateProductField(val field: ProductField) : StockEvent()
        data class UpdateCategoriaField(val field: CategoriaField) : StockEvent()
        object AddCategory : StockEvent()
    }

    // Campos de formulario de producto
    sealed class ProductField {
        data class Nombre(val value: String) : ProductField()
        data class CategoriaId(val value: String) : ProductField()
        data class Cantidad(val value: Double) : ProductField()
        data class Unidad(val value: UnidadMedida) : ProductField()
        data class StockMinimo(val value: Double) : ProductField()
        data class Precio(val value: Double) : ProductField()
    }

    // Campos de formulario de categoría
    sealed class CategoriaField {
        data class Nombre(val value: String) : CategoriaField()
    }
}