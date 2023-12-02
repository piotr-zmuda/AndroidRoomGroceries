package com.example.projektsmb.ui.theme

import com.example.projektsmb.Product

sealed interface ProductEvent{

    object SaveProduct: ProductEvent

    data class SetName(val name:String): ProductEvent
    data class SetPrice(val price:Double): ProductEvent

    object  ShowDialog: ProductEvent
    object HideDialog: ProductEvent
    data class SortProduct(val sortType:SortType): ProductEvent
    data class DeleteProduct(val product: Product):ProductEvent

    data class EditProduct(val product: Product):ProductEvent
}