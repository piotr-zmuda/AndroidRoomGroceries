package com.example.projektsmb



sealed interface ShopingCartEvent{

    object SaveShopingCart: ShopingCartEvent

    data class AddProduct(val product: Product): ShopingCartEvent

    object  ShowDialog: ShopingCartEvent
    object HideDialog: ShopingCartEvent

}