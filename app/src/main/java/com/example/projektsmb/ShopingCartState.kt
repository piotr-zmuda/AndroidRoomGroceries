package com.example.projektsmb

data class ShopingCartState(
    val shopingCarts : List<ShopingCart> = emptyList(),
    val products: List<Product> = emptyList(),
    val isAddingProduct: Boolean = false
)