package com.app.productcatalog.ui

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.productcatalog.domain.model.ProductSpec
import com.app.productcatalog.domain.repository.ProductRepository
import com.app.productcatalog.util.Event
import com.app.productcatalog.util.Result
import com.app.productcatalog.util.SchedulerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        insertProducts()
        refreshProducts()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertProducts() {
        viewModelScope.launch(schedulerProvider.default()) {
            productRepository.insertProducts()
                .onStart {
                    updateViewModel(_uiState.value.copy(isLoading = true))
                }
                .collect { result ->
                    when (result) {
                        is Result.Error -> {
                            updateViewModel(
                                _uiState.value.copy(
                                    isLoading = false,
                                    error = Event(
                                        result.exception ?: Exception("Telah Terjadi Kesalahan")
                                    )
                                )
                            )

                        }

                        is Result.Success -> {/* no-op */
                        }
                    }
                }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun refreshProducts() {
        viewModelScope.launch(schedulerProvider.default()) {
            val searchQuery = _uiState.value.currentSearchQuery
            val activeFilter = _uiState.value.activeFilter.value
            productRepository.getProducts(activeFilter, searchQuery)
                .onStart {
                    updateViewModel(_uiState.value.copy(isLoading = true))
                }
                .collect { result ->
                    when (result) {
                        is Result.Error -> {
                            updateViewModel(
                                _uiState.value.copy(
                                    isLoading = false,
                                    error = Event(
                                        result.exception ?: Exception("Telah Terjadi Kesalahan")
                                    )
                                )
                            )
                        }

                        is Result.Success ->  updateViewModel(
                            _uiState.value.copy(
                                isLoading = false,
                                successGetProducts = result.data
                            )
                        )
                    }
                }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun toggleFavouriteStatus(product: ProductSpec) {
        viewModelScope.launch(schedulerProvider.default()) {
            productRepository.updateProductFavourite(product)
                .onStart {
                    updateViewModel(_uiState.value.copy(isLoading = true))
                }
                .collect { result ->
                    when (result) {
                        is Result.Error -> {
                            updateViewModel(
                                _uiState.value.copy(
                                    isLoading = false,
                                    error = Event(
                                        result.exception ?: Exception("Telah Terjadi Kesalahan")
                                    )
                                )
                            )
                        }

                        is Result.Success -> {
                            val updatedList =
                                _uiState.value.successGetProducts.map { if (it.id == product.id) product else it }
                            updateViewModel(
                                _uiState.value.copy(
                                    isLoading = false, successGetProducts = updatedList
                                )
                            )
                        }
                    }
                }

        }
    }

    fun onEventChange(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.OnFavouritesProductSelected -> {
                val updatedProduct =
                    uiEvent.product.copy(isFavourite = !uiEvent.product.isFavourite)
                toggleFavouriteStatus(updatedProduct)
            }

            is UiEvent.OnUpdateSearchQuery -> {
                _uiState.update { it.copy(currentSearchQuery = uiEvent.query) }
                refreshProducts()
            }

            is UiEvent.OnFavouritesFilterClick -> {
                _uiState.update { it.copy(activeFilter = uiEvent.selectedFilter) }
                refreshProducts()
            }
        }
    }

    fun updateViewModel(newState: UiState) {
        _uiState.update { newState }
    }
}

sealed interface UiEvent {
    data class OnFavouritesProductSelected(val product: ProductSpec) : UiEvent
    data class OnUpdateSearchQuery(val query: String) : UiEvent
    data class OnFavouritesFilterClick(val selectedFilter: FilterOption) : UiEvent
}

data class UiState(
    val isLoading: Boolean = true,
    val activeFilter: FilterOption = FilterOption.All,
    val currentSearchQuery: String = "",
    val successGetProducts: List<ProductSpec> = emptyList(),
    val error: Event<Throwable>? = null
)