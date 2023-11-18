package com.example.projektsmb.ui.theme

import com.example.projektsmb.Product

data class ProductState(
    val products: List<Product> = emptyList(),
    val name:String = "",
    val price:Double = 0.0,
    val isAddingProduct: Boolean = false,
    val sortType: SortType = SortType.NAME
)
