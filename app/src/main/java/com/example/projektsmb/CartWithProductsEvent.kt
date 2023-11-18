package com.example.projektsmb

sealed interface CartWithProductsEvent {
    data class FetchCartProducts(val cartId: Long): CartWithProductsEvent
    data class UpdateCartProducts(val cartWithProducts: CartWithProducts): CartWithProductsEvent
    // Add other events related to CartWithProducts here
}
