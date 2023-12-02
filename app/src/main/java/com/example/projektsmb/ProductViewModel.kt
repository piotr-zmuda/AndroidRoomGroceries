package com.example.projektsmb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projektsmb.ui.theme.ProductEvent
import com.example.projektsmb.ui.theme.ProductState
import com.example.projektsmb.ui.theme.SortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(private val dao: ProductDao): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.NAME)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _products = _sortType.flatMapLatest {
        sortType -> when(sortType){
        SortType.NAME -> {
            dao.getProductOrderByName()
        }
        SortType.Price->{
            dao.getProductOrderByPrice()
        }
    }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ProductState())

    val state = combine(_state, _sortType, _products) { state, sortType, products -> state.copy(
        products = products,
        sortType = sortType
    )}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductState())

    fun onEvent(event: ProductEvent){
        when(event){
            is ProductEvent.DeleteProduct -> {
                viewModelScope.launch {
                    dao.deleteProduct(event.product)
                }
            }
            is ProductEvent.EditProduct ->{
                viewModelScope.launch {
                    dao.upsertProduct(event.product)
                }
            }
            ProductEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingProduct = false
                ) }
            }
            ProductEvent.SaveProduct -> {
                val name = state.value.name
                val price = state.value.price

                if(name.isBlank() || price.isNaN())
                    return

                val product = Product(name=name, price=price)
                viewModelScope.launch {
                    dao.upsertProduct(product)
                }
                _state.update { it.copy(isAddingProduct = false, name = "", price = 0.0) }
            }
            is ProductEvent.SetName -> {
                _state.update {it.copy(name = event.name)  }
            }
            is ProductEvent.SetPrice ->{
                _state.update { it.copy(price = event.price) }
            }
            ProductEvent.ShowDialog -> {
                _state.update { it.copy(isAddingProduct = true) }
            }
            is ProductEvent.SortProduct ->{
                _sortType.value = event.sortType
            }
        }
    }

}