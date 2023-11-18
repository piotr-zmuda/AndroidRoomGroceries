package com.example.projektsmb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projektsmb.ui.theme.ProductState
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShopingCartViewModel(
    private val dao: ShopingCartDao,
    private val productDao: ProductDao,
    private val cartProductCrossRefDao: CartProductCrossRefDao
):ViewModel() {

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())

    val allProducts: Flow<List<Product>> = productDao.getProductOrderByName()

    private val _state = MutableStateFlow(ShopingCartState())
    private val _shopingCarts = dao.getShopingCarts().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _shopingCarts) { state, _shopingCarts -> state.copy(
        shopingCarts = _shopingCarts
    )}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ShopingCartState())
    fun onEvent(event: ShopingCartEvent){
        when(event){
            is ShopingCartEvent.AddProduct -> {
                _state.update { currentState ->
                    val updatedProducts = currentState.products.toMutableList().apply {
                        add(event.product) // Add the new product to the list
                    }
                    currentState.copy(products = updatedProducts)
                }
            }
            ShopingCartEvent.SaveShopingCart -> {
                val products = state.value.products

                if (products.isEmpty()) return


                val productCountMap = products.groupBy { it.productId }
                    .mapValues { (_, productList) -> productList.size }

                // Create and save shopping cart entity
                val shoppingCart = ShopingCart()
                viewModelScope.launch {
                    val cartId = dao.upsertShopingCart(shoppingCart)

                    // Associate products with the newly created shopping cart
                    productCountMap.forEach { (productId, count) ->
                        val cartProductCrossRef = CartProductCrossRef(cartId, productId, count)
                        repeat(count) {
                            cartProductCrossRefDao.upsertCrossRef(cartProductCrossRef)
                        }
                    }
                }
                _state.update { it.copy(isAddingProduct = false, products = emptyList()) }
            }

            ShopingCartEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingProduct = false
                ) }
            }
            ShopingCartEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingProduct = true
                ) }
            }
        }


    }
}